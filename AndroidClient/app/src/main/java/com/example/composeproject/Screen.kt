package com.example.composeproject

sealed class Screen(val route: String) {

    object SplashScreen : Screen("splash_screen")
    object HomeScreen : Screen("home_screen")

    object AllRoutesScreen : Screen("all_routes_screen")
    object NewRouteScreen : Screen("new_route_screen")

    object RouteScreen: Screen("route_screen")
    object SegmentScreen: Screen("segment_screen")

    object AuthenticationScreen : Screen("authentication_screen")
    object LoginScreen : Screen("login_screen")
    object SignUpScreen : Screen("signup_screen")

    object AllSegmentsScreen : Screen("all_segments_screen")
    object NewSegmentScreen : Screen("new_segment_screen")

    object StatsScreen: Screen("stats_screen")
    object LeaderboardScreen : Screen("leaderboard_screen")
    object SegmentLeaderboardScreen : Screen("segment_leaderboard_screen")

}
