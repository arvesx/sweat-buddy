package com.example.composeproject.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.composeproject.BackendCommunicator
import com.example.composeproject.data.SegmentInfo
import com.example.composeproject.data.UserInfo
import com.example.composeproject.data.UserSegmentInfo
import com.example.composeproject.dependencies.fileprocessing.TransmissionObjectBuilder
import com.example.composeproject.dependencies.fileprocessing.TransmissionObjectType
import com.example.composeproject.dependencies.user.Segment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SegmentLeaderboardViewModel : ViewModel() {

    var userLead = mutableStateOf(listOf<UserSegmentInfo>())
    var curSegment = mutableStateOf(Segment())

    fun loadSegmentLeaderboard(navController: NavController, sharedViewModel: SharedViewModel)
    {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {

            val to = TransmissionObjectBuilder()
                .type(TransmissionObjectType.SEGMENT_LEADERBOARD)
                .segmentId(sharedViewModel.newSegmentId.value)
                .craft()

            val backendCommunicator = BackendCommunicator.getInstance()
            val answer = backendCommunicator.sendClientInfo(to)

            if (answer.success == 1)
            {
                //curSegment.value = answer.segmentObject

                var position = 1
                val newList = mutableListOf<UserSegmentInfo>()
                answer.segmentObject.leaderboard.forEach { item ->
                    val milliseconds: Long = item.totalTime
                    val minutes = milliseconds / 1000 / 60
                    val seconds = milliseconds / 1000 % 60
                    newList.add(UserSegmentInfo(item.userId, position, item.username, "${minutes}m${seconds}s"))
                    position++
                }

                withContext(Dispatchers.Main)
                {
                    userLead = mutableStateOf(newList)
                }
            }


        }





    }

}