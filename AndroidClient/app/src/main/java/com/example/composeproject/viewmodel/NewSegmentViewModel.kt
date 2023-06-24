package com.example.composeproject.viewmodel

import android.content.Context
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.composeproject.Navigation
import com.example.composeproject.Screen
import com.example.composeproject.BackendCommunicator
import com.example.composeproject.dependencies.fileprocessing.TransmissionObjectBuilder
import com.example.composeproject.dependencies.fileprocessing.TransmissionObjectType
import com.example.composeproject.dependencies.user.Route
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.*


class NewSegmentViewModel : ViewModel() {
    var isExpanded = mutableStateOf(false)
    var selectedRoute = mutableStateOf(Route())
    var textFieldValue = mutableStateOf(TextFieldValue(""))


    @OptIn(DelicateCoroutinesApi::class)
    fun onPressItem(
        navController: NavController,
        route: Route,
        onResponse: (List<LatLng>) -> Unit
    ) {
        selectedRoute.value = route
        isExpanded.value = false
        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                val newCoordinates: MutableList<LatLng> = mutableListOf()
                selectedRoute.value.coordinates.forEach { item ->
                    newCoordinates.add(LatLng(item.first, item.second))
                }
                onResponse(newCoordinates)
            }
        }

    }

    fun onCreate(navController: NavController, sharedViewModel: SharedViewModel) {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            val to = TransmissionObjectBuilder()
                .type(TransmissionObjectType.SEGMENT)
                .routeId(selectedRoute.value.routeId)
                .segmentStart(sharedViewModel.firstSegmentWaypointIndex)
                .segmentEnd(sharedViewModel.lastSegmentWaypointIndex)
                .craft()

            val backendCommunicator = BackendCommunicator.getInstance()
            val answer = backendCommunicator.sendClientInfo(to)

            if (answer.success == 1) {

                sharedViewModel.updateSpecificSegment(
                    textFieldValue.value.text,
                    answer.gpxResults.distanceInKilometers,
                    answer.gpxResults.totalAscentInMete,
                    answer.gpxResults.avgSpeedInKilometersPerHour,
                    answer.gpxResults.totalTimeInMillis
                )
                sharedViewModel.newSegmentId.value = answer.userData.segments.last().segmentId

                withContext(Dispatchers.Main) {
                    navController.navigate(Screen.SegmentScreen.route) {
                        popUpTo(0)
                    }
                }
            }
        }
    }
}