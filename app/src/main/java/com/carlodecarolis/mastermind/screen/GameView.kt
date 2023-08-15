package com.carlodecarolis.mastermind.screen

import android.content.res.Configuration
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.navigation.NavController

@Composable
fun GameView(navController: NavController) {
    val configuration = LocalConfiguration.current

    when (configuration.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> {
            //TODO
            Button(onClick = { /*TODO*/ }) {
                Text(text = "init")
            }
        }
        else -> {
        }
    }
}