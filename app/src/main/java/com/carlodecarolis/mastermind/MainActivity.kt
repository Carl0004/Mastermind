package com.carlodecarolis.mastermind

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.carlodecarolis.mastermind.db.DBMastermind
import com.carlodecarolis.mastermind.logic.GameViewModel
import com.carlodecarolis.mastermind.screen.MainScreen
import com.carlodecarolis.mastermind.ui.theme.MastermindTheme


class MainActivity : ComponentActivity() {
    private lateinit var gameViewModel: GameViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = DBMastermind.getInstance(applicationContext)
        gameViewModel = ViewModelProvider(this)[GameViewModel::class.java]

        setContent {
            AppContent(gameViewModel)
        }


    }
}

@Composable
fun AppContent(gameViewModel: GameViewModel) {
    MastermindTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            MainScreen(gameViewModel)
        }
    }
}

