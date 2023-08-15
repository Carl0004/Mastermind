package com.carlodecarolis.mastermind

import android.content.Context
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
import com.carlodecarolis.mastermind.db.Game
import com.carlodecarolis.mastermind.logic.GameViewModel
import com.carlodecarolis.mastermind.screen.MainScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader


class MainActivity : ComponentActivity() {
    private lateinit var gameViewModel: GameViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = DBMastermind.getInstance(applicationContext)
        gameViewModel = ViewModelProvider(this)[GameViewModel::class.java]

        CoroutineScope(Dispatchers.IO).launch {
            populateDatabaseFromFile(db, applicationContext, "testDB.txt")
        }
        setContent {
            AppContent(gameViewModel)
        }


    }

    private suspend fun populateDatabaseFromFile(db: DBMastermind, applicationContext: Context, fileName: String) {
        val inputStream: InputStream = applicationContext.assets.open(fileName)
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))

        val testDataList = mutableListOf<Game>()
        bufferedReader.useLines { lines ->
            lines.forEach { line ->
                val data = line.split(",") // Supponiamo che i dati siano separati da virgola
                if (data.size == 7) {
                    val game = Game(
                        id = data[0].toLong(),
                        version = data[1],
                        secretCode = data[2],
                        result = data[3],
                        attempts = data[4].toInt(),
                        duration = data[5].toLong(),
                        date = data[6].toLong()
                    )
                    testDataList.add(game)

                    println("Read from file and inserted game: $game") // Aggiungi questa istruzione per la stampa a video
                }
            }
        }

        db.daoGameHistory().insertGameHistoryList(testDataList)
    }
}

@Composable
fun AppContent(gameViewModel: GameViewModel) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        MainScreen(gameViewModel)
    }
}

