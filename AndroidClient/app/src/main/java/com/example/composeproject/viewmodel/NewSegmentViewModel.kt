package com.example.composeproject.viewmodel

import android.content.Context
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.composeproject.Navigation
import com.example.composeproject.Screen
import com.example.composeproject.dependencies.user.Route
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.*
import okhttp3.Dispatcher


class NewSegmentViewModel : ViewModel()
{
    var isExpanded = mutableStateOf(false)
    var selectedRoute = mutableStateOf(Route())

    @OptIn(DelicateCoroutinesApi::class)
    fun onPressItem(
        navController: NavController,
        route: Route,
        onResponse: (List<LatLng>) -> Unit
    )
    {
        selectedRoute.value = route
        isExpanded.value = false
        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                val newCoordinates: MutableList<LatLng> = mutableListOf()
                selectedRoute.value.coordinates.forEach {item ->
                    newCoordinates.add(LatLng(item.first, item.second))
                }
                onResponse(newCoordinates)
            }
        }

    }

    fun onCreate(
        navController: NavController,
        sharedViewModel: SharedViewModel
    )
    {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {

            withContext(Dispatchers.Main)
            {
                navController.navigate(Screen.SegmentScreen.route) {
                    popUpTo(0)
                }
            }

        }

    }

    fun onLeaderboardClick(navController: NavController, sharedViewModel: SharedViewModel) {

    }

}