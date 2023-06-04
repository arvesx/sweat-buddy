package com.example.composeproject.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.composeproject.dependencies.fileprocessing.TransmissionObject
import com.example.composeproject.dependencies.user.Route

class SharedViewModel : ViewModel() {

    var username = mutableStateOf("")
    var routes = mutableStateOf(listOf<Route>())
    var mostRecentRouteKm = mutableStateOf(0.0)
    var totalNumberOfRoutes = mutableStateOf(0)
    var totalKilometeres = mutableStateOf(0.0f)

    // specific route info
    var totalDistance = mutableStateOf(0.0f)
    var totalElevation = mutableStateOf(0.0f)
    var avgSpeed = mutableStateOf(0.0f)
    var totalTime = mutableStateOf("0m")


    fun updateViewModel(to: TransmissionObject) {

        if (to.userData.routes.isNotEmpty()) {
            routes = mutableStateOf(to.userData.routes)
            mostRecentRouteKm.value = routes.value.last().totalDistanceInKm
            totalNumberOfRoutes.value = to.userData.routesDoneThisMonth
            totalKilometeres.value = to.userData.totalKmThisMonth
        }


    }

    fun updateSpecificRoute(
        totalDistance: Double,
        totalElevation: Double,
        avgSpeed: Double,
        totalTime: Long
    ) {
        val milliseconds: Long = totalTime
        val minutes = milliseconds / 1000 / 60
        val seconds = milliseconds / 1000 % 60


        this.totalDistance.value = totalDistance.toFloat()
        this.totalElevation.value = totalElevation.toFloat()
        this.avgSpeed.value = avgSpeed.toFloat()
        this.totalTime.value = "${minutes}m${seconds}s"
    }
}

