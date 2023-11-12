package com.khanalytic.database.shared

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import com.khanalytic.models.Brand as ModelBrand
import com.khanalytic.models.PlatformBrand as ModelPlatformBrand
import com.khanalytic.models.UserPlatformCookie as ModelUserPlatformCookie
import kotlinx.datetime.Instant
import kotlinx.serialization.json.Json

class BrandDao: KoinComponent {
    private val database: Database by inject()

    suspend fun upsetBrands(brands: List<ModelBrand>) = withContext(Dispatchers.IO) {
        database.dbQuery.transaction {
            brands.forEach {
                database.dbQuery.upsetBrand(it.id, it.name, it.address, it.latitude(), it.longitude(),
                    it.createdAt.toString(), it.updatedAt.toString())

                it.platformBrands.forEach { platformBrand ->
                    database.dbQuery.upsertPlatformBrandWithoutCookie(
                        platformBrand.id,
                        platformBrand.brandId,
                        platformBrand.platformId,
                        platformBrand.remoteBrandId,
                        platformBrand.active
                    )
                }
            }
        }
    }

    suspend fun upsetBrands(brands: List<ModelBrand>, userPlatformCookieId: Long) =
        withContext(Dispatchers.IO) {
            database.dbQuery.transaction {
                brands.forEach {
                    database.dbQuery.upsetBrand(it.id, it.name, it.address, it.latitude(),
                        it.longitude(), it.createdAt.toString(), it.updatedAt.toString())

                    it.platformBrands.forEach { platformBrand ->
                        database.dbQuery.upsertPlatformBrand(
                            platformBrand.id,
                            platformBrand.brandId,
                            platformBrand.platformId,
                            platformBrand.remoteBrandId,
                            platformBrand.active,
                            userPlatformCookieId
                        )
                    }
                }
            }
        }

    suspend fun selectAllBrands(): List<ModelBrand> = withContext(Dispatchers.IO) {
        database.dbQuery.selectAllBrands().executeAsList()
            .groupBy { it.brandId }
            .values
            .map { it.toModelBrand() }
    }

    suspend fun selectAllBrandsByCookieId(
        userPlatformCookieId: Long
    ): List<ModelBrand> = withContext(Dispatchers.IO) {
        database.dbQuery.selectAllBrandsByCookieId(userPlatformCookieId).executeAsList()
            .groupBy { it.brandId }
            .values
            .map { it.toCookieModelBrand() }
    }

    fun selectAllBrandsAsFlow(): Flow<List<ModelBrand>> =
        database.dbQuery.selectAllBrands()
            .asFlow()
            .flowOn(Dispatchers.IO)
            .mapToList()
            .map {
                it.groupBy { it.brandId }
                .values
                .map { selectAllBrands -> selectAllBrands.toModelBrand() }
            }

    suspend fun getAllRemoteBrandIdsByActive(platformId: Long): Map<String, Boolean> =
        withContext(Dispatchers.IO) {
            database.dbQuery.selectAllRemoteBrandIdsWithActive(platformId).executeAsList()
                .associate { Pair(it.remoteBrandId, it.active) }
        }

    suspend fun lastSyncedBrand(): ModelBrand? = withContext(Dispatchers.IO) {
        database.dbQuery.lastSyncedBrand().executeAsOneOrNull()?.let { it.toModelBrand() }
    }

    suspend fun getPlatformBrandsWithCookies(

    ): List<Pair<ModelPlatformBrand, ModelUserPlatformCookie>> =
        withContext(Dispatchers.IO) {
            database.dbQuery.selectPlatformBrandsWithCookies().executeAsList().map {
                val platformBrand = ModelPlatformBrand(
                    id = it.id,
                    brandId = it.brandId,
                    platformId = it.platformId,
                    remoteBrandId = it.remoteBrandId,
                    active = it.active,
                    userPlatformCookieId = it.userPlatformCookieId
                )
                val userPlatformCookie = ModelUserPlatformCookie(
                    id = it.id_,
                    userId = it.userId,
                    platformId = it.platformId_,
                    cookies = serializer.decodeFromString(it.cookiesJson)
                )
                Pair(platformBrand, userPlatformCookie)
            }
        }

    suspend fun getActivePlatformBrandsByCookieId(
        userPlatformCookieId: Long
    ): List<ModelPlatformBrand> =
        withContext(Dispatchers.IO) {
            database.dbQuery.selectActivePlatformBrandsByCookieId(userPlatformCookieId)
                .executeAsList()
                .map {
                    ModelPlatformBrand(
                        id = it.id,
                        brandId = it.brandId,
                        platformId = it.platformId,
                        remoteBrandId = it.remoteBrandId,
                        active = it.active,
                        userPlatformCookieId = it.userPlatformCookieId
                    )
                }
        }

    companion object {
        private val serializer = Json

        private fun List<SelectAllBrands>.toModelBrand(): ModelBrand =
            ModelBrand(
                id = first().id,
                name = first().name,
                address = first().address,
                latitude = first().latitude,
                longitude = first().longitude,
                createdAt = Instant.parse(first().createdAt),
                updatedAt = Instant.parse(first().updatedAt),
                platformBrands = this.map { selectAllBrand ->
                    ModelPlatformBrand(
                        id = selectAllBrand.id_,
                        brandId = selectAllBrand.brandId,
                        remoteBrandId = selectAllBrand.remoteBrandId,
                        platformId = selectAllBrand.platformId,
                        active = selectAllBrand.active,
                        userPlatformCookieId = selectAllBrand.userPlatformCookieId
                    )
                }
            )

        private fun List<SelectAllBrandsByCookieId>.toCookieModelBrand(): ModelBrand =
            ModelBrand(
                id = first().id,
                name = first().name,
                address = first().address,
                latitude = first().latitude,
                longitude = first().longitude,
                createdAt = Instant.parse(first().createdAt),
                updatedAt = Instant.parse(first().updatedAt),
                platformBrands = this.map { selectAllBrand ->
                    ModelPlatformBrand(
                        id = selectAllBrand.id_,
                        brandId = selectAllBrand.brandId,
                        remoteBrandId = selectAllBrand.remoteBrandId,
                        platformId = selectAllBrand.platformId,
                        active = selectAllBrand.active,
                        userPlatformCookieId = selectAllBrand.userPlatformCookieId
                    )
                }
            )

        private fun Brand.toModelBrand(): ModelBrand = ModelBrand(
            id = id,
            name = name,
            address = address,
            latitude = latitude,
            longitude = longitude,
            createdAt = Instant.parse(createdAt),
            updatedAt = Instant.parse(updatedAt),
            platformBrands = listOf()
        )
    }
}