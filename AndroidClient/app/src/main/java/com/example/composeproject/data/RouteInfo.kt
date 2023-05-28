package com.example.composeproject.data

data class RouteInfo(
    val id: Int,
    val title: String,
    val type: Int, // 0 for hike, 1 for walk, 2 for run
    val daysDoneAgo: Int
)