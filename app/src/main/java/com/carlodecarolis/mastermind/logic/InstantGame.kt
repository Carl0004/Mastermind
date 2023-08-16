package com.carlodecarolis.mastermind.logic

import androidx.compose.runtime.mutableStateOf
import com.carlodecarolis.mastermind.db.Game
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*

class InstantGame(private val gameViewModel: GameViewModel){
    private var currentId: Long = -1
    private val colorOptions = listOf("W", "R", "C", "G", "Y", "P", "O", "B")
    private var startTime = System.currentTimeMillis()

    val maxAttempts = 10

    var secret = ""
    var attempts = 0
    var duration = 0L
    var date = ""

    var isGameFinished = mutableStateOf(false)

    init {
        CoroutineScope(Dispatchers.IO).launch {
            currentId = gameViewModel.getNextId()
        }

        newMatch()
    }
    fun newMatch() {
        secret = generateRandomSecret()
        attempts = 0
        duration = 0L
        date = formatDate(System.currentTimeMillis())
        isGameFinished.value = false
        startTime = System.currentTimeMillis() // Resetta il tempo all'inizio della partita
    }

    fun attempt(input: String): String {
        if (input.length != secret.length) {
            return "Invalid input"
        }

        attempts++

        if (attempts >= maxAttempts) {
            isGameFinished.value = true
            saveOnDb()
            return "Game Over"
        }

        if (isCorrect(input)) {
            isGameFinished.value = true
            saveOnDb()
            return "Correct Combination!"
        }

        return getFeedback(input)
    }

    private fun generateRandomSecret(): String {
        return buildString {
            repeat(8) {
                append(colorOptions.random())
            }
        }
    }
    fun generateNextId(): Long {
        return currentId++
    }
    private fun isCorrect(input: String): Boolean {
        return input == secret
    }

    private fun getFeedback(input: String): String {
        val secretSet = secret.toSet()
        val inputSet = input.toSet()

        val correctPositions = input.zip(secret).count { (guess, actual) -> guess == actual }
        val correctColors = inputSet.intersect(secretSet).count() - correctPositions

        return "Positions: $correctPositions, Colors: $correctColors"
    }

    fun saveOnDb() {
        duration = System.currentTimeMillis() - startTime // Calcola la durata della partita
        val game = Game(
            id = generateNextId(),
            version = "1.0", // Modifica con la versione dell'app
            secretCode = secret,
            result = "Game Over", // Puoi personalizzare il messaggio di fine gioco
            attempts = attempts,
            duration = duration,
            date = System.currentTimeMillis()
        )

        runBlocking { gameViewModel.insertGameHistory(game) }    }

    private fun formatDate(timestamp: Long): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        return dateFormat.format(calendar.time)
    }
}