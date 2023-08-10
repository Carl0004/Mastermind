package com.carlodecarolis.mastermind.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper (context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "MastermindDatabase"
        private const val TABLE_NAME = "games"
        private const val KEY_ID = "id"
        private const val KEY_SECRET_CODE = "secret_code"
        private const val KEY_GUESS_SEQUENCE = "guess_sequence"
        private const val KEY_DURATION = "duration"
        private const val KEY_DATE = "date"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME " +
                "($KEY_ID INTEGER PRIMARY KEY, " +
                "$KEY_SECRET_CODE TEXT, " +
                "$KEY_GUESS_SEQUENCE TEXT, " +
                "$KEY_DURATION INTEGER, " +
                "$KEY_DATE TEXT)"
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addGame(secretCode: String, guessSequence: String, duration: Long, date: String) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_SECRET_CODE, secretCode)
        values.put(KEY_GUESS_SEQUENCE, guessSequence)
        values.put(KEY_DURATION, duration)
        values.put(KEY_DATE, date)
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    @SuppressLint("Range")
    fun getAllGames(): List<Game> {
        val gameList = mutableListOf<Game>()
        val selectQuery = "SELECT * FROM $TABLE_NAME ORDER BY $KEY_ID DESC"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                val secretCode = cursor.getString(cursor.getColumnIndex(KEY_SECRET_CODE))
                val guessSequence = cursor.getString(cursor.getColumnIndex(KEY_GUESS_SEQUENCE))
                val duration = cursor.getLong(cursor.getColumnIndex(KEY_DURATION))
                val date = cursor.getString(cursor.getColumnIndex(KEY_DATE))
                val game = Game(id, secretCode, guessSequence, duration, date)
                gameList.add(game)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return gameList
    }
}