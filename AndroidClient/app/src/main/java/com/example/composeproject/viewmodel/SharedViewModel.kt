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

    fun updateViewModel(to: TransmissionObject) {

        if (to.userData.routes.isNotEmpty()) {
            routes = mutableStateOf(to.userData.routes)
            mostRecentRouteKm.value = routes.value.last().totalDistanceInKm
            totalNumberOfRoutes.value = to.userData.routesDoneThisMonth
            totalKilometeres.value = to.userData.totalKmThisMonth
        }


    }

}