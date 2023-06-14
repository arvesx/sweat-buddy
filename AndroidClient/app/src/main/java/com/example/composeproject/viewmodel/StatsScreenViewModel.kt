package com.example.composeproject.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.composeproject.BackendCommunicator
import com.example.composeproject.dependencies.fileprocessing.TransmissionObjectBuilder
import com.example.composeproject.dependencies.fileprocessing.TransmissionObjectType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StatsScreenViewModel : ViewModel() {

    var totalDistance = mutableStateOf(0.0f)
    var totalElevation = mutableStateOf(0.0f)
    var totalTime = mutableStateOf("0m")
    var totalTimeMillis = mutableStateOf(0L)
    var avgTotalDistance = mutableStateOf(0.0f)
    var avgTotalElevation = mutableStateOf(0.0f)
    var avgTotalTime = mutableStateOf("0m")
    var avgTotalTimeMillis = mutableStateOf(0L)

    fun onPageLoad() {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            val backendCommunicator = BackendCommunicator.getInstance()

            val to = TransmissionObjectBuilder()
                .type(TransmissionObjectType.GENERIC_STATS_REQUEST)
                .craft()

            val answer = backendCommunicator.sendClientInfo(to)

            withContext(Dispatchers.Main) {
                if (answer.success == 1) {
                    val receivedStats = answer.genericStats
                    totalDistance.value = receivedStats.totalDistance
                    totalElevation.value = receivedStats.totalElevation
                    avgTotalDistance.value = receivedStats.avgTotalDistance
                    avgTotalElevation.value = receivedStats.avgTotalElevation

                    totalTimeMillis.value = receivedStats.totalTime
                    avgTotalTimeMillis.value = receivedStats.avgTotalTime

                    var milliseconds: Long = receivedStats.totalTime
                    var minutes = milliseconds / 1000 / 60
                    var seconds = milliseconds / 1000 % 60


                    totalTime.value = "${minutes}m${seconds}s"

                    milliseconds = receivedStats.avgTotalTime
                    minutes = milliseconds / 1000 / 60
                    seconds = milliseconds / 1000 % 60
                    avgTotalTime.value = "${minutes}m${seconds}s"
                }
            }
        }
    }
}