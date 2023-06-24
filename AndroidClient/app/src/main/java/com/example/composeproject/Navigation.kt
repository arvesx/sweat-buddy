package com.example.composeproject

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.composeproject.viewmodel.SharedViewModel

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val sharedViewModel = SharedViewModel()

    NavHost(navController = navController, startDestination = Screen.SplashScreen.route)
    {
        composable(Screen.SplashScreen.route) {
            SplashScreen(navController)
        }

        composable(Screen.HomeScreen.route) {
            HomeScreen(navController, sharedViewModel)
        }

        composable(Screen.AllRoutesScreen.route) {
            RoutesScreen(navController = navController, sharedViewModel)
        }

        composable(Screen.NewRouteScreen.route) {
            NewRouteScreen(navController = navController, sharedViewModel)
        }

        composable(Screen.AuthenticationScreen.route) {
            AuthenticationScreen(navController = navController)
        }

        composable(Screen.LoginScreen.route) {
            Login(navController = navController, sharedViewModel)
        }
        composable(Screen.SignUpScreen.route) {
            Signup(navController = navController, sharedViewModel)
        }

        composable(Screen.RouteScreen.route) {
            NewRouteCreatedScreen(navController, sharedViewModel)
        }

        composable(Screen.SegmentScreen.route) {
            NewSegmentCreatedScreen(navController, sharedViewModel)
        }

        composable(Screen.AllSegmentsScreen.route) {
            SegmentsScreen(navController, sharedViewModel)
        }

        composable(Screen.NewSegmentScreen.route) {
            NewSegmentScreen(navController, sharedViewModel)
        }

        composable(Screen.StatsScreen.route) {
            StatsScreen(navController = navController, sharedViewModel)
        }

        composable(Screen.LeaderboardScreen.route) {
            LeaderboardScreen(navController, sharedViewModel)
        }

        composable(Screen.SegmentLeaderboardScreen.route) {
            SegmentLeaderboardScreen(navController, sharedViewModel)
        }

    }
}



