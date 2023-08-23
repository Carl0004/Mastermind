package com.carlodecarolis.mastermind.logic

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.carlodecarolis.mastermind.db.Game
import com.carlodecarolis.mastermind.db.Repository
import com.carlodecarolis.mastermind.logic.utils.Attempt
import com.carlodecarolis.mastermind.logic.utils.Feedback
import com.carlodecarolis.mastermind.logic.utils.GameState
import com.carlodecarolis.mastermind.logic.utils.Options
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

/*class InstantGame(private val gameViewModel: GameViewModel) {
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
            repeat(5) {
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
}*/

class InstantGame(private val repository: Repository) {
    var secret = mutableStateOf("")
    var attempts = mutableStateListOf<Attempt>()
    var startTime = System.currentTimeMillis()
    var duration =  mutableStateOf(0L)
    var date = mutableStateOf("")
    var isGameFinished = mutableStateOf(false)
    var status = mutableStateOf(GameState.Load)
    var life = mutableStateOf(10)
    var currentId: Long = -1L
    val colorOptions = listOf("W", "R", "C", "G", "Y", "P", "O", "B")
    var showFeedback = mutableStateOf(false)


    init {
        CoroutineScope(Dispatchers.IO).launch {
            currentId = repository.getNextId() ?: 1L
        }
    }


    fun calculateFeedback(guess: String): String {
        val nrr: Int = attempts.lastOrNull()?.rightNumRightPos ?: 0
        val nrw: Int = attempts.lastOrNull()?.rightNumWrongPos ?: 0

        return "RR: $nrr, RW: $nrw"
    }

    fun newMatch() {
        secret.value = generateRandomSecret()
        attempts.clear() // Resetta la lista di tentativi
        duration.value = 0L
        date.value = formatDate(System.currentTimeMillis())
        isGameFinished.value = false
        startTime = System.currentTimeMillis()
        status.value = GameState.Ongoing
    }
    private fun generateRandomSecret(): String {
        return buildString {
            repeat(5) {
                append(colorOptions.random())
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
        var nrr: Int = 0
        var nrw: Int = 0
        var newSecret = ""
        var newGuess = ""
        var attempt: Attempt
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

        if (!newSecret.isEmpty()) {
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
        showFeedback.value = true
    }

    private fun countHowMany(letters: String, letter: Char): Int {
        var howMany = 0
        for (i in 0 until letters.length) {
            if (letters[i] == letter) {
                howMany++
            }
        }
        return howMany
    }




    fun saveOnDb() {
        duration.value = System.currentTimeMillis() - startTime
        val game = Game(
            id = currentId++,
            version = "1.0",
            secretCode = secret.value,
            result = if (isGameFinished.value) "Game Over" else "Correct Combination!",
            attempts = attempts.size,
            duration = duration.value,
            date = System.currentTimeMillis()
        )

        runBlocking{
            withContext(Dispatchers.IO) {
                repository.insert(game)
            }
        }
    }


    fun loadMatch() {
        //TODO se ci va
    }
}

