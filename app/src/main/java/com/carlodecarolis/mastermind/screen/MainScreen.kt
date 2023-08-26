package com.carlodecarolis.mastermind.screen

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.carlodecarolis.mastermind.logic.MyViewModel

@Composable
fun MainScreen(gameViewModel: MyViewModel) {
    val navController = rememberNavController()
    val dispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    NavHost(navController = navController, startDestination = "Home") {
        composable("Home") { Home(gameViewModel, navController) }
        composable("GameView") {
            if (dispatcher != null) {
                GameView(navController, gameViewModel, dispatcher)
            }
        }
        composable("History"){ History(gameViewModel)}
        composable("Settings") {Settings(gameViewModel, navController)}
    }
}


