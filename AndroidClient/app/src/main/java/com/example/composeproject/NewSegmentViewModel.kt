package com.example.composeproject

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.composeproject.dependencies.user.Route
import com.example.composeproject.viewmodel.SharedViewModel


class NewSegmentViewModel : ViewModel()
{
    var isExpanded = mutableStateOf(false)
    var selectedRoute = mutableStateOf(Route())

    fun onPressItem(navController: NavController, route: Route)
    {
        selectedRoute.value = route
        isExpanded.value = false
    }



}