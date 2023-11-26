package com.khanalytic.kmm.ui.screens.anaytics

import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.khanalytic.database.shared.UserDao
import com.khanalytic.kmm.http.requests.UserApiRequest
import com.khanalytic.kmm.http.requests.toRequest
import com.khanalytic.kmm.http.requests.toUserAuthRequest
import com.khanalytic.kmm.http.responses.AnalyticsReport
import com.khanalytic.kmm.ui.screens.filter.AnalyticsFilter
import com.khanalytic.kmm.ui.screens.filter.AnalyticsFilterManager
import com.khanalytic.models.InstantUtils.toLocateDateUtc
import com.khanalytic.models.User
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.minus
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

open class AnalyticsPagerScreenModel<T>(
    private val screenType: ScreenType<T>
): ScreenModel, KoinComponent {
    protected val analyticsFilterManager: AnalyticsFilterManager by inject()
    private val userDao: UserDao by inject()
    private lateinit var user: User
    private val mutex = Mutex()

    val analyticsPagesFlow = MutableStateFlow(listOf<AnalyticsPage>())
    val appliedAnalyticsFilterFlow = MutableStateFlow<AnalyticsFilter?>(null)
    private val dataItems = mutableListOf<AnalyticsReport<T>>()
    private var isDataLoading = false
    private var allDataLoaded = false
    private var existingJob: Deferred<Unit>? = null

    init {
        screenModelScope.launch {
            updateAnalyticsPages()
            if (!skipLoadingDuringInit()) { initLoading() }
        }
    }

    /**
     * Subclasses should override this method if they want to avoid loading during the init.
     *
     * It is expected that the subclasses will then call initLoading() after their initialization.
     */
    open fun skipLoadingDuringInit(): Boolean = false

    /** Subclasses should override this method if they want to override the global filters. */
    open fun analyticsFilterFlow(): Flow<AnalyticsFilter?> =
        analyticsFilterManager.filterFlow()

    suspend fun tryLoadNextBatch(filter: AnalyticsFilter) {
        val isAlreadyLoadingOrLoaded = isDataLoading || allDataLoaded
        if (isAlreadyLoadingOrLoaded) { return }
        isDataLoading = true
        if (!::user.isInitialized) {
            userDao.getFirstUser()?.let { user = it }
        }
        if (!::user.isInitialized) {
            allDataLoaded = true
        } else { loadNextBatch(filter) }
        isDataLoading = false
    }

    protected suspend fun initLoading() = coroutineScope {
        launch {
            analyticsFilterFlow().collect { filter ->
                existingJob?.cancel()
                isDataLoading = false
                allDataLoaded = false
                dataItems.clear()
                updateAnalyticsPages()
                appliedAnalyticsFilterFlow.value = filter
                if (filter != null) { existingJob = async { tryLoadNextBatch(filter) } }
            }
        }
    }

    private suspend fun loadNextBatch(filter: AnalyticsFilter) {
        val date = dataItems.lastOrNull()?.date?.minus(1, DateTimeUnit.DAY)
            ?: Clock.System.now().toLocateDateUtc()
        val request = UserApiRequest(filter.toRequest(date), user.toUserAuthRequest())
        val items = screenType.api.report(request).reports
        dataItems.addAll(items.sortedBy { it.date }.reversed())
        if (items.size < request.requestData.limit) { allDataLoaded = true }
        updateAnalyticsPages()
    }

    private fun updateAnalyticsPages() {
        val pages = mutableListOf<AnalyticsPage>()
        if (allDataLoaded && dataItems.isEmpty()) { pages.add(AnalyticsPage.NoDataAvailable) }
        pages.addAll(dataItems.map { AnalyticsPage.Data(it) })
        if (!allDataLoaded) { pages.add(AnalyticsPage.Loading) }
        analyticsPagesFlow.value = pages
    }
}