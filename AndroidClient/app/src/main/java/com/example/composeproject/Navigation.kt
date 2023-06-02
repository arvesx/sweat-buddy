package com.example.composeproject

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@OptIn(DelicateCoroutinesApi::class)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun Navigation() {
    GlobalScope.launch {
        val backend = BackendCommunicator.getInstance()
        backend.attemptConnection()
    }

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.SplashScreen.route)
    {
        composable(Screen.SplashScreen.route) {
            SplashScreen(navController)
        }

        composable(Screen.HomeScreen.route) {
            HomeScreen(navController)
        }

        composable(Screen.AllRoutesScreen.route) {
            RoutesScreen(navController = navController)
        }

        composable(Screen.NewRouteScreen.route) {
            NewRouteScreen(navController = navController)
        }

        composable(Screen.AuthenticationScreen.route) {
            AuthenticationScreen(navController = navController)
        }

        composable(Screen.LoginScreen.route) {
            Login(navController = navController)
        }
        composable(Screen.SignUpScreen.route) {
            Signup(navController = navController)
        }
    }
}



