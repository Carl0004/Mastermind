package com.carlodecarolis.mastermind.db

import androidx.room.*

@Dao
interface MastermindDao {
    @Insert
    suspend fun insertGameHistory(gameHistory: Game)

    @Delete
    suspend fun deleteGameHistory(game: Game)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGameHistoryList(gameHistoryList: List<Game>)

    @Query("SELECT * FROM game_history")
    fun getAllGameHistory(): List<Game>
}