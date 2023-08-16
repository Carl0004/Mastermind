package com.carlodecarolis.mastermind.db
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository(private val dao: MastermindDao) {
    suspend fun readAll(): List<Game> {
        return withContext(Dispatchers.IO) {
            dao.getAllGameHistory()
        }
    }

    suspend fun insert(game: Game) {
        withContext(Dispatchers.IO) {
            dao.insertGameHistory(game)
        }
    }
    suspend fun getNextId(): Long {
        return withContext(Dispatchers.IO) {
            dao.getNextId()
        }
    }
    suspend fun deleteGameHistory(game: Game) {
        withContext(Dispatchers.IO) {
            dao.deleteGameHistory(game)
        }
    }

    suspend fun deleteAllGameHistory() {
        withContext(Dispatchers.IO) {
            dao.deleteAllGameHistory()
        }
    }
}