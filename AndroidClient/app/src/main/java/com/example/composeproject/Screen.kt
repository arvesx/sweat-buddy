package com.example.composeproject

sealed class Screen(val route: String) {

    object SplashScreen: Screen("splash_screen")
    object HomeScreen: Screen("home_screen")
}
