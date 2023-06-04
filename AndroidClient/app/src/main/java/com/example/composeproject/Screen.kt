package com.example.composeproject

sealed class Screen(val route: String) {

    object SplashScreen : Screen("splash_screen")
    object HomeScreen : Screen("home_screen")

    object AllRoutesScreen : Screen("all_routes_screen")
    object NewRouteScreen : Screen("new_route_screen")

    object RouteScreen: Screen("route_screen")

    object AuthenticationScreen : Screen("authentication_screen")
    object LoginScreen : Screen("login_screen")
    object SignUpScreen : Screen("signup_screen")
}
