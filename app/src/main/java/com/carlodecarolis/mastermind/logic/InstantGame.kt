package com.carlodecarolis.mastermind.logic

import androidx.compose.runtime.mutableStateOf
import com.carlodecarolis.mastermind.db.Game
import com.carlodecarolis.mastermind.logic.utils.Attempt
import com.carlodecarolis.mastermind.logic.utils.Feedback
import com.carlodecarolis.mastermind.logic.utils.Options
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*

class InstantGame(private val gameViewModel: GameViewModel) {
    private var currentId: Long = -1
    val colorOptions = listOf(
        "W",
        "R",
        "C",
        "G",
        "Y",
        "P",
        "O",
        "B")


    private var startTime = System.currentTimeMillis()
    val maxAttempts = 10

    var secret = ""
    var attempts = mutableListOf<Attempt>() // Lista di tentativi
    var duration = 0L
    var date = ""

    var isGameFinished = mutableStateOf(false)

    init {
        CoroutineScope(Dispatchers.IO).launch {
            currentId = gameViewModel.getNextId()?:-1
        }

        newMatch()
    }

    fun newMatch() {
        secret = generateRandomSecret()
        attempts.clear() // Resetta la lista di tentativi
        duration = 0L
        date = formatDate(System.currentTimeMillis())
        isGameFinished.value = false
        startTime = System.currentTimeMillis()
    }

    fun attempt(input: List<Options>) {
        if (input.size != secret.length) {
            return
        }

        attempts.add(Attempt(input, emptyList()))

        if (attempts.size >= maxAttempts) {
            isGameFinished.value = true
            saveOnDb()
        }

        if (isCorrect(input)) {
            isGameFinished.value = true
            saveOnDb()
        }
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

    fun isCorrect(input: List<Options>): Boolean {
        val inputColors = input.map { it.color }
        return inputColors == secret.toList()
    }

    fun saveOnDb() {
        duration = System.currentTimeMillis() - startTime
        val game = Game(
            id = generateNextId(),
            version = "1.0",
            secretCode = secret,
            result = if (isGameFinished.value) "Game Over" else "Correct Combination!",
            attempts = attempts.size,
            duration = duration,
            date = System.currentTimeMillis()
        )

        runBlocking { gameViewModel.insertGameHistory(game) }
    }

    private fun formatDate(timestamp: Long): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        return dateFormat.format(calendar.time)
    }
}

