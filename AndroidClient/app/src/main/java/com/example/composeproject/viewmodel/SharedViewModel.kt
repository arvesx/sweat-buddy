package com.example.composeproject.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.composeproject.Screen
import com.example.composeproject.data.ContentCardType
import com.example.composeproject.data.UserInfo
import com.example.composeproject.dependencies.fileprocessing.TransmissionObject
import com.example.composeproject.dependencies.fileprocessing.gpx.WaypointImpl
import com.example.composeproject.dependencies.user.Route
import com.example.composeproject.dependencies.user.Segment
import com.example.composeproject.dependencies.user.SegmentAttempt
import com.example.composeproject.dependencies.user.UserData
import com.example.composeproject.utils.calculateCameraPosition
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.abs

class SharedViewModel : ViewModel() {

    var userID = mutableStateOf(0)
    var username = mutableStateOf("")
    var routes = mutableStateOf(listOf<Route>())
    var segments = mutableStateOf(listOf<SegmentAttempt>())
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
    var routeWaypoints = mutableStateOf(listOf<LatLng>())
    var cameraPositionState = mutableStateOf(CameraPositionState())

    var newSegmentId = mutableStateOf(0)

    // specific segment info
    var totalDistanceS = mutableStateOf(0.0f)
    var totalElevationS = mutableStateOf(0.0f)
    var avgSpeedS = mutableStateOf(0.0f)
    var totalTimeS = mutableStateOf("0m")
    var routeNameS = mutableStateOf("")


    // about segments
    var firstSegmentWaypoint = mutableStateOf(LatLng(0.0, 0.0))
    var lastSegmentWaypoint = mutableStateOf(LatLng(0.0, 0.0))

    var firstSegmentWaypointIndex = 0
    var lastSegmentWaypointIndex = 0

    var selectedSegmentLatLngList: List<LatLng> = listOf()

    //lateinit var userData: UserData
    var userData: UserData = UserData()

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
        val startIndex = coordinates.indexOf(firstSegmentWaypoint.value)
        val lastIndex = coordinates.indexOf(lastSegmentWaypoint.value) + 1
        firstSegmentWaypointIndex = startIndex
        lastSegmentWaypointIndex = lastIndex
        selectedSegmentLatLngList = coordinates.subList(startIndex, lastIndex)
        println("Start Wp index: $firstSegmentWaypointIndex")
        println("End Wp index: $lastSegmentWaypointIndex")
    }

    fun updateRouteCoordinates(coordinatesLatLng: List<LatLng>) {

        routes.value.last().coordinatesLatLng = coordinatesLatLng
    }

    fun updateViewModel(to: TransmissionObject) {

        userID.value = to.userData.userId
        if (to.userData.routes.isNotEmpty()) {
            routes = mutableStateOf(to.userData.routes)
            segments = mutableStateOf(to.userData.segments)
            mostRecentRouteKm.value = routes.value.last().totalDistanceInKm
            totalNumberOfRoutes.value = to.userData.routesDoneThisMonth
            totalKilometeres.value = to.userData.totalKmThisMonth
            this.userData = to.userData
        }


    }

    fun updateSpecificRoute(
        routeName: String,
        routePoints: Int,
        totalDistance: Double,
        totalElevation: Double,
        avgSpeed: Double,
        totalTime: Long,
        routeWps: List<WaypointImpl>
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

        val wps = mutableListOf<LatLng>()
        for (i in routeWps) {
            wps.add(LatLng(i.latitude, i.longitude))
        }
        routeWaypoints.value = wps
        cameraPositionState.value = CameraPositionState(
            position = CameraPosition.fromLatLngZoom(
                calculateCameraPosition(wps),
                14.0f
            )
        )
    }

    fun handleContentCardClick(
        cardType: ContentCardType,
        navController: NavController,
        itemId: Int,
        sharedViewModel: SharedViewModel
    ) {

        val scope = CoroutineScope(Dispatchers.Default)
        scope.launch {
            if (cardType == ContentCardType.ROUTE) {
                val route: Route = getRouteByID(itemId)
                updateSpecificRoute(
                    route.routeName,
                    route.points,
                    route.totalDistanceInKm,
                    route.totalElevationInM,
                    route.averageSpeedInKmH,
                    route.totalTimeInMillis,
                    route.routeWaypoints
                )

                withContext(Dispatchers.Main) {
                    navController.navigate(Screen.RouteScreen.route) {
                        popUpTo(0)
                    }
                }
            }
            else
            {
                val segmentAttempt: SegmentAttempt = getSegmentByID(itemId)
                updateSpecificSegment(
                    segmentAttempt.segmentName,
                    segmentAttempt.totalDistance.toDouble(),
                    segmentAttempt.elevation.toDouble(),
                    segmentAttempt.avgSpeed.toDouble(),
                    segmentAttempt.totalTime
                )
                sharedViewModel.newSegmentId.value = itemId

                withContext(Dispatchers.Main) {
                    navController.navigate(Screen.SegmentScreen.route) {
                        popUpTo(0)
                    }
                }
            }
        }
    }

    fun updateSpecificSegment(
        routeNameS: String,
        totalDistanceS: Double,
        totalElevationS: Double,
        avgSpeedS: Double,
        totalTimeS: Long
    ) {
        this.routeNameS.value = routeNameS

        val milliseconds: Long = totalTimeS
        val minutes = milliseconds / 1000 / 60
        val seconds = milliseconds / 1000 % 60

        this.totalDistanceS.value = totalDistanceS.toFloat()
        this.totalElevationS.value = totalElevationS.toFloat()
        this.avgSpeedS.value = avgSpeedS.toFloat()
        this.totalTimeS.value = "${minutes}m${seconds}s"
    }

    fun getRouteByID(routeId: Int): Route {
        for (route in userData.routes) {
            if (route.routeId == routeId) return route
        }
        return Route(0)
    }

    fun getSegmentByID(segmentId: Int): SegmentAttempt {
        for (segment in userData.segments) {
            if (segment.segmentId == segmentId) return segment
        }
        return SegmentAttempt()
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

    fun clearData()
    {
        userID = mutableStateOf(0)
        username = mutableStateOf("")
        routes = mutableStateOf(listOf())
        segments = mutableStateOf(listOf())
        leaderboardList = mutableStateOf(listOf())
        mostRecentRouteKm = mutableStateOf(0.0)
        totalNumberOfRoutes = mutableStateOf(0)
        totalKilometeres = mutableStateOf(0.0f)
        totalDistance = mutableStateOf(0.0f)
        totalElevation = mutableStateOf(0.0f)
        avgSpeed = mutableStateOf(0.0f)
        totalTime = mutableStateOf("0m")
        routeName = mutableStateOf("")
        routePoints = mutableStateOf(0)
        routeWaypoints = mutableStateOf(listOf())
        cameraPositionState = mutableStateOf(CameraPositionState())
        newSegmentId = mutableStateOf(0)
        totalDistanceS = mutableStateOf(0.0f)
        totalElevationS = mutableStateOf(0.0f)
        avgSpeedS = mutableStateOf(0.0f)
        totalTimeS = mutableStateOf("0m")
        routeNameS = mutableStateOf("")
        firstSegmentWaypoint = mutableStateOf(LatLng(0.0, 0.0))
        lastSegmentWaypoint = mutableStateOf(LatLng(0.0, 0.0))
        firstSegmentWaypointIndex = 0
        lastSegmentWaypointIndex = 0
        selectedSegmentLatLngList = listOf()
        userData = UserData()
    }


}



