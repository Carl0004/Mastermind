package com.carlodecarolis.mastermind.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_history")
data class Game(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val version: String, //Versione app
    val secretCode: String, //Codice segreto partita
    var result: String, //Risultato partita
    var attempts: Int, //# di tentativi
    var duration: Long, //Tempo impiegato
    val date: Long, //Data partita
    var isSelected: Boolean = false // per selezionare
)