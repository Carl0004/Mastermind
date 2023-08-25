package com.carlodecarolis.mastermind.screen

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.carlodecarolis.mastermind.logic.MyViewModel

@Composable
fun MainScreen(gameViewModel: MyViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "Home") {
        composable("Home") { Home(gameViewModel, navController) }
        composable("GameView") { GameView(navController, gameViewModel) }
        composable("History"){ History(gameViewModel)}
        composable("Settings") {Settings(gameViewModel, navController)}
    }
}


