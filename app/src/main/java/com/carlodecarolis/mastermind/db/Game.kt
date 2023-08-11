package com.carlodecarolis.mastermind.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_history")
data class Game(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val version: String, //Versione app
    val secretCode: String, //Codice segreto partita
    val result: String, //Risultato partita
    val attempts: Int, //# di tentativi
    val duration: Long, //Tempo impiegato
    val date: Long //Data partita
)