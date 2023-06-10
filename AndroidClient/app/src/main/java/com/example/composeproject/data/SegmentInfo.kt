package com.example.composeproject.data

data class SegmentInfo(
    override val id: Int,
    override val title: String,
    override val type: Int, // 0 for hike, 1 for walk, 2 for run
    val daysDoneAgo: Int
) : Info(id, title, type)