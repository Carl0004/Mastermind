package com.carlodecarolis.mastermind.screen

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.carlodecarolis.mastermind.logic.GameViewModel
import com.carlodecarolis.mastermind.logic.InstantGame

@Composable
fun MainScreen(gameViewModel: GameViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "Home") {
        composable("Home") { Home(navController) }
        composable("GameView") { GameView(navController, gameViewModel) }
        composable("History"){ History(navController, gameViewModel)}
    }
}


