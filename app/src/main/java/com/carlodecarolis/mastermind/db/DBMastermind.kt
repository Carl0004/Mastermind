package com.carlodecarolis.mastermind.db

import android.content.Context
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase

@Database(entities = [Game::class], version = 1)
abstract class DBMastermind : RoomDatabase(){
    abstract fun DaoGameHistory() : MastermindDao

    companion object{
        @Volatile
        private var db: DBMastermind? = null

        fun getInstance(context: Context): DBMastermind{
            if(db == null){
                db = databaseBuilder(
                    context,
                    DBMastermind::class.java,
                    "game_history_database"
                )
                    .build()
            }
            return db as DBMastermind
        }
    }
}