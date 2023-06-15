package com.example.composeproject.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.composeproject.data.UserInfo
import com.example.composeproject.dependencies.fileprocessing.TransmissionObject
import com.example.composeproject.dependencies.user.Route
import com.example.composeproject.dependencies.user.Segment
import com.example.composeproject.dependencies.user.UserData
import com.google.android.gms.maps.model.LatLng

class SharedViewModel : ViewModel() {

    var userID = mutableStateOf(0)
    var username = mutableStateOf("")
    var routes = mutableStateOf(listOf<Route>())
    var segments = mutableStateOf(listOf<Segment>())
    var leaderboardList = mutableStateOf(listOf<UserInfo>())
    var mostRecentRouteKm = mutableStateOf(0.0)
    var totalNumberOfRoutes = mutableStateOf(0)
    var totalKilometeres = mutableStateOf(0.0f)

    // specific route info
    var totalDistance = mutableStateOf(0.0f)
    var totalElevation = mutableStateOf(0.0f)
    var avgSpeed = mutableStateOf(0.0f)
    var totalTime = mutableStateOf("0m")
    var routeName = mutableStateOf("")

    fun updateRouteCoordinates(coordinatesLatLng: List<LatLng>) {

        routes.value.last().coordinatesLatLng = coordinatesLatLng
    }

    fun updateViewModel(to: TransmissionObject) {

        userID.value = to.userData.userId
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

    fun getUserDataByID(userID: Int): UserInfo {
        if (leaderboardList.value.isNotEmpty())
        {
            leaderboardList.value.forEach {item ->
                if (item.userID == userID)
                {
                    return item
                }
            }
        }
        return UserInfo(0, 0, "", 0)
    }

    fun getUserDataByPosition(userPosition: Int): UserInfo {
        if (leaderboardList.value.isNotEmpty())
        {
            leaderboardList.value.forEach {item ->
                if (item.position == userPosition)
                {
                    return item
                }
            }
        }
        return UserInfo(0, 0, "", 0)
    }

}

