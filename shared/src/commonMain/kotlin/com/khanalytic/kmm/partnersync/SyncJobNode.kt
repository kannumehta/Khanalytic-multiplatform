package com.khanalytic.kmm.partnersync

sealed class SyncJobNode {
    data class InternalNode(
        val title: String,
        val children: MutableList<SyncJobNode> = mutableListOf()
    ): SyncJobNode()

    data class LeafNode(val title: String, var status: SyncJobStatus): SyncJobNode()
}

enum class SyncJobStatus {
    Pending,
    Processing,
    Processed,
    Failed
}

fun SyncJobNode.status(): SyncJobStatus = when (this) {
    is SyncJobNode.LeafNode -> status
    is SyncJobNode.InternalNode -> {
        if (children.isEmpty()) {
            SyncJobStatus.Pending
        } else if (children.any { it.status() == SyncJobStatus.Processing }) {
            SyncJobStatus.Processing
        } else if (children.any { it.status() == SyncJobStatus.Failed }) {
            SyncJobStatus.Failed
        } else if (children.filterNot { it.status() == SyncJobStatus.Processed }.isEmpty()) {
            SyncJobStatus.Processed
        } else {
            SyncJobStatus.Pending
        }
    }
}

fun SyncJobNode.title(): String = when (this) {
    is SyncJobNode.LeafNode -> title
    is SyncJobNode.InternalNode -> title
}

fun SyncJobNode.toUiList(): List<Pair<Int, SyncJobNode>> =
    if (this is SyncJobNode.InternalNode) {
        children.flatMap { it.toUiList(1) }
    } else {
        listOf()
    }

fun SyncJobNode.toUiList(level: Int): List<Pair<Int, SyncJobNode>> {
    val nodesList = mutableListOf<Pair<Int, SyncJobNode>>()
    nodesList.add(Pair(level, this))
    if (this is SyncJobNode.InternalNode) {
        if (status() == SyncJobStatus.Failed || status() == SyncJobStatus.Processing) {
            children.map { child -> nodesList.addAll(child.toUiList(level + 1)) }
        }
    }
    return nodesList
}

fun SyncJobNode.clone(): SyncJobNode = when(this) {
    is SyncJobNode.LeafNode -> SyncJobNode.LeafNode(title, status)
    is SyncJobNode.InternalNode ->
        SyncJobNode.InternalNode(title, children.map { it.clone() }.toMutableList())
}
