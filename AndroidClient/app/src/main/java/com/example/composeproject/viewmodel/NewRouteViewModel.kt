package com.example.composeproject.viewmodel

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.composeproject.BackendCommunicator
import com.example.composeproject.Screen
import com.example.composeproject.dependencies.fileprocessing.TransmissionObjectBuilder
import com.example.composeproject.dependencies.fileprocessing.TransmissionObjectType
import com.example.composeproject.dependencies.fileprocessing.gpx.WaypointImpl
import com.example.composeproject.dependencies.user.UserData
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream

class NewRouteViewModel : ViewModel() {
    var pathChosen = mutableStateOf("")
    var textFieldValue = mutableStateOf(TextFieldValue(""))

    fun onCreate(
        navController: NavController,
        sharedViewModel: SharedViewModel,
        context: Context,
        coordinates: List<LatLng>
    ) {

        if (pathChosen.value.isNotEmpty() && textFieldValue.value.text.isNotEmpty()) {
            val scope = CoroutineScope(Dispatchers.IO)
            scope.launch {
                val gpxString = getGpxString(context)
                val coordinatesPair: MutableList<Pair<Double, Double>> = mutableListOf()
                coordinates.forEach { item ->
                    coordinatesPair.add(Pair(item.latitude, item.longitude))
                }

                val to = TransmissionObjectBuilder()
                    .type(TransmissionObjectType.GPX_FILE)
                    .gpxFile(gpxString)
                    .coordinates(coordinatesPair)
                    .message(textFieldValue.value.text)
                    .craft()

                val backendCommunicator = BackendCommunicator.getInstance()
                val answer = backendCommunicator.sendClientInfo(to)

                val coordinatesLatLng: MutableList<LatLng> = mutableListOf()
                coordinatesPair.forEach { item ->
                    coordinatesLatLng.add(LatLng(item.first, item.second))
                }

                var currentUserPoints = 0
                sharedViewModel.routes.value.forEach { item ->
                    currentUserPoints += item.points
                }

                if (answer.success == 1) {

                    val newUserPoints = answer.userData.points - currentUserPoints

                    val temporaryWps = mutableListOf<WaypointImpl>()
                    for (i in coordinates) {
                        temporaryWps.add(WaypointImpl(i.longitude, i.latitude, 0.0, 0))
                    }

                    sharedViewModel.updateViewModel(answer)
                    sharedViewModel.updateRouteCoordinates(coordinatesLatLng)
                    sharedViewModel.updateSpecificRoute(
                        textFieldValue.value.text,
                        newUserPoints,
                        answer.gpxResults.distanceInKilometers,
                        answer.gpxResults.totalAscentInMete,
                        answer.gpxResults.avgSpeedInKilometersPerHour,
                        answer.gpxResults.totalTimeInMillis,
                        temporaryWps
                    )

                    withContext(Dispatchers.Main) {
                        navController.navigate(Screen.RouteScreen.route) {
                            popUpTo(0)
                        }
                    }
                }

            }
        }
    }

    private fun getGpxString(context: Context): String? {
        val input: InputStream? =
            context.contentResolver.openInputStream(Uri.parse(pathChosen.value))

        return input?.bufferedReader().use { it?.readText() }
    }
}