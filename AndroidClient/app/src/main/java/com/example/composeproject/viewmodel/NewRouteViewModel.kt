package com.example.composeproject.viewmodel

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.composeproject.BackendCommunicator
import com.example.composeproject.Screen
import com.example.composeproject.dependencies.fileprocessing.TransmissionObjectBuilder
import com.example.composeproject.dependencies.fileprocessing.TransmissionObjectType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream

class NewRouteViewModel : ViewModel() {
    var pathChosen = mutableStateOf("")
    var textFieldValue = mutableStateOf(TextFieldValue(""))

    fun onCreate(navController: NavController, sharedViewModel: SharedViewModel, context: Context) {

        if (pathChosen.value.isNotEmpty() && textFieldValue.value.text.isNotEmpty()) {
            val scope = CoroutineScope(Dispatchers.IO)
            scope.launch {
                val gpxString = getGpxString(context)

                val to = TransmissionObjectBuilder()
                    .type(TransmissionObjectType.GPX_FILE)
                    .gpxFile(gpxString)
                    .message(textFieldValue.value.text)
                    .craft()

                val backendCommunicator = BackendCommunicator.getInstance()
                val answer = backendCommunicator.sendClientInfo(to)


                if (answer.success == 1) {
                    sharedViewModel.updateViewModel(answer)
                    sharedViewModel.updateSpecificRoute(
                        answer.gpxResults.distanceInKilometers,
                        answer.gpxResults.totalAscentInMete,
                        answer.gpxResults.avgSpeedInKilometersPerHour,
                        answer.gpxResults.totalTimeInMillis
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