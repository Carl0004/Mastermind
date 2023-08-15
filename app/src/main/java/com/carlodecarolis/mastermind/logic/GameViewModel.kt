package com.carlodecarolis.mastermind.logic

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.carlodecarolis.mastermind.db.DBMastermind
import com.carlodecarolis.mastermind.db.Game
import com.carlodecarolis.mastermind.db.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GameViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: Repository

    init {
        val dao = DBMastermind.getInstance(application).daoGameHistory()
        repository = Repository(dao)
    }

    suspend fun getAllGameHistory(): List<Game> {
        return withContext(Dispatchers.IO) {
            repository.readAll()
        }
    }

    suspend fun insertGameHistory(game: Game) {
        withContext(Dispatchers.IO) {
            repository.insert(game)
        }
    }

    suspend fun deleteGameHistory(game: Game) {
        withContext(Dispatchers.IO) {
            repository.delete(game)
        }
    }
}