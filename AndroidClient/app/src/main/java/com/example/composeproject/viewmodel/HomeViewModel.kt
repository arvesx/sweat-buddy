package com.example.composeproject.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.composeproject.BackendCommunicator
import com.example.composeproject.Screen
import com.example.composeproject.data.UserInfo
import com.example.composeproject.dependencies.fileprocessing.TransmissionObjectBuilder
import com.example.composeproject.dependencies.fileprocessing.TransmissionObjectType
import com.example.composeproject.dependencies.user.Route
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher

class HomeViewModel : ViewModel() {

    fun onLogoutClick(navController: NavController, sharedViewModel: SharedViewModel) {

        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            val to = TransmissionObjectBuilder()
                .type(TransmissionObjectType.LOGOUT_REQUEST)
                .craft()

            val backendCommunicator = BackendCommunicator.getInstance()
            val answer = backendCommunicator.sendClientInfo(to)

            if (answer.success == 1) {

                withContext(Dispatchers.Main)
                {
                    navController.navigate(Screen.AuthenticationScreen.route) {
                        popUpTo(0)
                    }


                }

                delay(500L)

                // Clear data
                sharedViewModel.clearData()
            }
        }




    }

    fun onLeaderboardClick(navController: NavController, sharedViewModel: SharedViewModel) {

        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            val to = TransmissionObjectBuilder()
                .type(TransmissionObjectType.LEADERBOARD)
                .craft()

            val backendCommunicator = BackendCommunicator.getInstance()
            val answer = backendCommunicator.sendClientInfo(to)

            if (answer.success == 1) {
                var position = 1
                val newList = mutableListOf<UserInfo>()
                answer.leaderboardList.forEach { item ->
                    newList.add(UserInfo(item.userId, position, item.name, item.points))
                    position++
                }

                withContext(Dispatchers.Main)
                {
                    sharedViewModel.leaderboardList.value = newList
                }
            }
        }
    }

}