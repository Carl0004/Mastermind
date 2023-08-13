package com.carlodecarolis.mastermind

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.carlodecarolis.mastermind.db.*
import com.carlodecarolis.mastermind.logic.*
import com.carlodecarolis.mastermind.screen.*
import com.carlodecarolis.mastermind.ui.theme.MasterMindTheme



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MasterMindTheme {
                val context = LocalContext.current
                val db =  DBMastermind.getInstance(context)
                val repository = Repository(db.DaoGameHistory())
                val instantGame = InstantGame()
                val vm : GameViewModel = GameViewModel(instantGame, repository)

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    when (vm.state.value) {
                        MyState.Init -> Home(vm)
                        MyState.NewGame -> GameView(vm)
                        MyState.History -> History(vm)
                    }
                }
            }
        }
    }
}