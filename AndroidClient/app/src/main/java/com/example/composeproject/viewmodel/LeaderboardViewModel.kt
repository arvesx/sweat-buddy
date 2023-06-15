package com.example.composeproject.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.composeproject.data.UserInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LeaderboardViewModel : ViewModel() {

    var leadUsers = mutableStateOf(listOf<UserInfo>())

    fun loadLeaderboard(navController: NavController, sharedViewModel: SharedViewModel) {

        val scope = CoroutineScope(Dispatchers.Main)
        scope.launch {
            leadUsers.value = sharedViewModel.leaderboardList.value
        }

    }


}