package com.khanalytic.kmm.http.requests

import com.khanalytic.models.InstantSerializer
import com.khanalytic.models.InstantUtils.toLocalDateTimeUtc
import com.khanalytic.models.User
import io.ktor.http.ParametersBuilder
import kotlinx.datetime.Instant
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class SyncRequest(
    @Serializable(with = InstantSerializer::class) val lastUpdatedAt: Instant,
    val idOffset: Long,
    val limit: Int = 100
)

fun ParametersBuilder.appendSyncRequest(request: SyncRequest) {
    append("request_data[id_offset]", request.idOffset.toString())
    append("request_data[limit]", request.limit.toString())
    append("request_data[last_updated_at]",
        request.lastUpdatedAt.toLocalDateTimeUtc().toString())
}

fun ParametersBuilder.appendSyncRequestWithUser(request: SyncRequest, user: User) {
    append("user[email]", user.email)
    append("user[auth_token]", user.authToken)
    append("request_data[id_offset]", request.idOffset.toString())
    append("request_data[limit]", request.limit.toString())
    append("request_data[last_updated_at]",
        request.lastUpdatedAt.toLocalDateTimeUtc().toString())
}