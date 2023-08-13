package com.carlodecarolis.mastermind.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MastermindDao {
    @Insert
    suspend fun insertGameHistory(gameHistory: Game)

    @Delete
    suspend fun deleteGameHistory(game: Game)


    @Query("SELECT * FROM game_history ORDER BY date DESC")
    fun getAllGameHistory(): Game?
}