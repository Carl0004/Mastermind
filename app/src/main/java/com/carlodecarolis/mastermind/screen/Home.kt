package com.carlodecarolis.mastermind.screen

import android.content.res.Configuration
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import com.carlodecarolis.mastermind.logic.GameViewModel

@Composable
fun Home(vm: GameViewModel) {
    val configuration = LocalConfiguration.current

    when (configuration.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> {
            //TODO
            Button(onClick = { vm.hi() }) {
                Text(text = "history")
            }
        }
        else -> {
        }
    }
}