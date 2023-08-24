package com.carlodecarolis.mastermind.logic

import com.carlodecarolis.mastermind.db.Game
import com.carlodecarolis.mastermind.db.Repository
import com.carlodecarolis.mastermind.logic.utils.GameState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.absoluteValue

class MyViewModel(inGame: InstantGame, repo: Repository) {
    val instantGame : InstantGame
    val repository : Repository
    //var state = mutableStateOf(Init)
    var n = 0
    var isDatabaseSaved = false

    init {
        instantGame = inGame
        repository = repo
    }

    fun newGame(){
        if (instantGame.status.value != GameState.Ongoing) {
            instantGame.newMatch()
            isDatabaseSaved = false
            CoroutineScope(Dispatchers.Default).launch {
                while (true) {
                    if (instantGame.status.value == GameState.Ongoing)
                        instantGame.duration.value =
                            System.currentTimeMillis() - instantGame.startTime.absoluteValue
                    Thread.sleep(500)
                }
            }
        }
    }

    suspend fun getAllGameHistory(): List<Game> {
        return withContext(Dispatchers.IO) {
            repository.readAll()
        }
    }

    suspend fun deleteSelectedGames(selectedGames: List<Game>) {
        for (game in selectedGames) {
            repository.deleteGameHistory(game)
        }
    }
}