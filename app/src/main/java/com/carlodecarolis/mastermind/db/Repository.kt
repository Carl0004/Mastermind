package com.carlodecarolis.mastermind.db
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Repository(private val dao: MastermindDao) {

    fun readAll(): Game? {
        return dao.getAllGameHistory()
    }

    fun insert(game: Game){
        CoroutineScope(Dispatchers.IO).launch{ dao.insertGameHistory(game) }
    }

    fun delete(game: Game){
        CoroutineScope(Dispatchers.IO).launch{ dao.deleteGameHistory(game) }
    }
}