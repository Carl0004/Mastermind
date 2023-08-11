package com.carlodecarolis.mastermind.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MastermindDao {
    @Insert
    suspend fun insertGameHistory(gameHistory: Game)

    @Query("SELECT * FROM game_history ORDER BY date DESC")
    suspend fun getAllGameHistory(): List<Game>
}