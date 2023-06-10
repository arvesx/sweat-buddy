package com.example.composeproject.data

open class Info(
    open val id: Int,
    open val title: String,
    open val type: Int, // 0 for hike, 1 for walk, 2 for run
)