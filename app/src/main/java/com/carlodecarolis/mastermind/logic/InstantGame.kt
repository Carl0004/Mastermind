package com.carlodecarolis.mastermind.logic

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.carlodecarolis.mastermind.db.Game
import com.carlodecarolis.mastermind.db.Repository
import com.carlodecarolis.mastermind.logic.utils.Attempt
import com.carlodecarolis.mastermind.logic.utils.GameState
import com.carlodecarolis.mastermind.screen.Difficulty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class InstantGame(private val repository: Repository) {
    var secret = mutableStateOf("")
    var attempts = mutableStateListOf<Attempt>()
    var startTime = System.currentTimeMillis()
    var duration =  mutableStateOf(0L)
    private var date = mutableStateOf("")
    var isGameFinished = mutableStateOf(false)
    var status = mutableStateOf(GameState.Load)
    private var life = mutableStateOf(10)
    private var currentId: Long = -1L
    val colorOptions = mutableListOf("W", "R", "C", "G", "Y", "P", "O", "B")
    var difficulty by mutableStateOf(Difficulty.Normal)

    init {
        CoroutineScope(Dispatchers.IO).launch {
            currentId = repository.getNextId() ?: 1L
        }
    }


    fun newMatch() {
        secret.value = generateRandomSecret(difficulty)
        attempts.clear() // Resetta la lista di tentativi
        duration.value = 0L
        date.value = formatDate(System.currentTimeMillis())
        isGameFinished.value = false
        startTime = System.currentTimeMillis()
        life.value = 10
        status.value = GameState.Ongoing
    }

    private fun generateRandomSecret(difficulty: Difficulty): String {
        return if (difficulty == Difficulty.Normal) {
            buildString {
                repeat(5) {
                    append(colorOptions.random())
                }
            }
        } else {
            // Modalità Facile: Genera un codice segreto senza colori ripetuti
            val shuffledColors = colorOptions.shuffled().distinct()
            if (shuffledColors.size >= 5) {
                return shuffledColors.take(5).joinToString(separator = "")
            } else {
                return ""
            }
        }
    }

    private fun formatDate(timestamp: Long): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        return dateFormat.format(calendar.time)
    }


    fun attempt(guess: String) {
        var nrr = 0
        var nrw = 0
        var newSecret = ""
        var newGuess = ""
        val evaluatedChars = mutableListOf<Char>()

        // Numero di cifre giuste al posto giusto
        for (i in 0 until secret.value.length) {
            if (secret.value[i] == guess[i]) {
                nrr++
            }
        }

        // Numero di cifre giuste al posto sbagliato
        for (i in 0 until secret.value.length) {
            if (secret.value[i] != guess[i]) {
                newSecret += secret.value[i]
                newGuess += guess[i]
            }
        }

        if (newSecret.isNotEmpty()) {
            for (letter in guess) {
                if (!evaluatedChars.contains(letter)) {
                    val howManyInSecret = countHowMany(newSecret, letter)
                    val howManyInGuess = countHowMany(newGuess, letter)

                    nrw += if (howManyInSecret == howManyInGuess || howManyInSecret > howManyInGuess) howManyInGuess
                    else howManyInSecret

                    evaluatedChars.add(letter)
                }
            }
        }

        attempts.add(Attempt(guess, nrr, nrw))
        life.value -= 1


        if (nrr == secret.value.length) {
            status.value = GameState.Win
            isGameFinished.value = true
        }
        else if (life.value < 1) {
            status.value = GameState.Lose
            isGameFinished.value = true
        }

    }

    private fun countHowMany(letters: String, letter: Char): Int {
        var howMany = 0
        for (element in letters) {
            if (element == letter) {
                howMany++
            }
        }
        return howMany
    }


    fun saveOnDb(repository: Repository) {
        val game = Game(
            id = currentId++,
            version = "1.0",
            secretCode = secret.value,
            result = if (isGameFinished.value) "Game Over" else "Correct Combination!",
            attempts = attempts.size,
            duration = duration.value,
            date = System.currentTimeMillis()
        )

        CoroutineScope(Dispatchers.IO).launch {
            repository.insert(game)
        }

    }
}

