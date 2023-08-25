package com.carlodecarolis.mastermind

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.carlodecarolis.mastermind.db.DBMastermind
import com.carlodecarolis.mastermind.db.Repository
import com.carlodecarolis.mastermind.logic.InstantGame
import com.carlodecarolis.mastermind.logic.MyViewModel
import com.carlodecarolis.mastermind.screen.MainScreen
import com.carlodecarolis.mastermind.ui.theme.Black200
import com.carlodecarolis.mastermind.ui.theme.MastermindTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MastermindTheme {
                val context = LocalContext.current
                val db =  DBMastermind.getInstance(context)
                val repository = Repository(db.daoGameHistory())
                val instantGame = InstantGame(repository)
                //val vm : MyViewModel = ViewModelProvider(this)[MyViewModel(instantGame,repository,Application())::class.java]
                val vm = MyViewModel(instantGame, repository)
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Black200
                ) {
                    MainScreen(vm)
                }
            }
        }
    }
}

