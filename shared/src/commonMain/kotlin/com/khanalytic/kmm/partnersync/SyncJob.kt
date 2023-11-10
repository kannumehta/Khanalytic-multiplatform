package com.khanalytic.kmm.partnersync

data class SyncJob(
    var title: String,
    var status: SyncJobStatus = SyncJobStatus.Pending,
    val children: List<SyncJob> = listOf()
)

enum class SyncJobStatus {
    Pending,
    Processing,
    Processed,
    Failed
}