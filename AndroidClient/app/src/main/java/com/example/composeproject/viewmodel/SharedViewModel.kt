package com.example.composeproject.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.composeproject.data.UserInfo
import com.example.composeproject.dependencies.fileprocessing.TransmissionObject
import com.example.composeproject.dependencies.user.Route
import com.example.composeproject.dependencies.user.Segment
import com.example.composeproject.dependencies.user.UserData
import com.google.android.gms.maps.model.LatLng
import kotlin.math.abs

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
    var routePoints = mutableStateOf(0)


    // about segments
    var firstSegmentWaypoint = mutableStateOf(LatLng(0.0, 0.0))
    var lastSegmentWaypoint = mutableStateOf(LatLng(0.0, 0.0))

    var selectedSegmentLatLngList: List<LatLng> = listOf()

    fun rightExtendSegment(wp: LatLng, coordinates: List<LatLng>) {
        lastSegmentWaypoint.value = wp
        updateSegment(coordinates)
    }

    fun leftExtendSegment(wp: LatLng, coordinates: List<LatLng>) {
        firstSegmentWaypoint.value = wp
        updateSegment(coordinates)
    }

    fun shrinkSegment(wp: LatLng, coordinates: List<LatLng>) {
        val segmentStartPos = coordinates.indexOf(firstSegmentWaypoint.value)
        val segmentEndPos = coordinates.indexOf(lastSegmentWaypoint.value)
        val clickedWpPos = coordinates.indexOf(wp)

        val leftDistance = abs(segmentStartPos - clickedWpPos)
        val rightDistance = abs(segmentEndPos - clickedWpPos)
        if (leftDistance < rightDistance) {
            firstSegmentWaypoint.value = wp
        } else {
            lastSegmentWaypoint.value = wp
        }
        updateSegment(coordinates)
    }

    fun updateSegment(coordinates: List<LatLng>) {
        selectedSegmentLatLngList = coordinates.subList(
            coordinates.indexOf(firstSegmentWaypoint.value),
            coordinates.indexOf(lastSegmentWaypoint.value) + 1
        )
        print(selectedSegmentLatLngList)
    }

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
        routeName: String,
        routePoints: Int,
        totalDistance: Double,
        totalElevation: Double,
        avgSpeed: Double,
        totalTime: Long
    ) {
        this.routeName.value = routeName
        this.routePoints.value = routePoints

        val milliseconds: Long = totalTime
        val minutes = milliseconds / 1000 / 60
        val seconds = milliseconds / 1000 % 60


        this.totalDistance.value = totalDistance.toFloat()
        this.totalElevation.value = totalElevation.toFloat()
        this.avgSpeed.value = avgSpeed.toFloat()
        this.totalTime.value = "${minutes}m${seconds}s"
    }

    fun getUserDataByID(userID: Int): UserInfo {
        if (leaderboardList.value.isNotEmpty()) {
            leaderboardList.value.forEach { item ->
                if (item.userID == userID) {
                    return item
                }
            }
        }
        return UserInfo(0, 0, "", 0)
    }

    fun getUserDataByPosition(userPosition: Int): UserInfo {
        if (leaderboardList.value.isNotEmpty()) {
            leaderboardList.value.forEach { item ->
                if (item.position == userPosition) {
                    return item
                }
            }
        }
        return UserInfo(0, 0, "", 0)
    }

}

