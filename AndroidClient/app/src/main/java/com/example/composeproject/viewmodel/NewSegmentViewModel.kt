package com.example.composeproject.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.composeproject.dependencies.user.Route
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.*


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

    )
    {

    }



}