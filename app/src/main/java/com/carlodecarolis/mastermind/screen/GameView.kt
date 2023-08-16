package com.carlodecarolis.mastermind.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.carlodecarolis.mastermind.db.Game
import com.carlodecarolis.mastermind.logic.GameViewModel
import com.carlodecarolis.mastermind.logic.InstantGame
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameView(
    navController: NavController,
    gameViewModel: GameViewModel
) {
    val instantGame = remember { InstantGame(gameViewModel) }
    val userInputState = remember { mutableStateOf(TextFieldValue("")) }
    val feedbackState = remember { mutableStateOf("") }
    val isGameFinished = remember { mutableStateOf(false) }
    val isInputEnabled = remember { mutableStateOf(true) }

    val maxAttempts = instantGame.maxAttempts

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Mastermind",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Attempts: ${instantGame.attempts}/$maxAttempts",
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = userInputState.value.text,
            onValueChange = { newValue ->
                if (isInputEnabled.value && newValue.length <= 8) {
                    userInputState.value = TextFieldValue(newValue)
                }
            },
            label = { Text(text = "Enter the combination") },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            textStyle = TextStyle(fontSize = 16.sp),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Characters
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (isInputEnabled.value && !isGameFinished.value) {
                        val result = instantGame.attempt(userInputState.value.text)
                        feedbackState.value = result
                        isGameFinished.value = instantGame.isGameFinished
                        isInputEnabled.value = !isGameFinished.value
                        userInputState.value = TextFieldValue("") // Reset the input field
                    }
                }
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = feedbackState.value,
            color = if (feedbackState.value == "Correct Combination!") Color.Green else Color.Red,
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        )

        if (isGameFinished.value) {
            Text(
                text = "Secret Code: ${instantGame.secret}",
                fontSize = 18.sp,
                color = Color.Gray,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                textAlign = TextAlign.Center
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        instantGame.newMatch()
                        feedbackState.value = ""
                        isGameFinished.value = false
                        isInputEnabled.value = true
                    },
                    enabled = !isGameFinished.value
                ) {
                    Text(text = "New Game")
                }
                Button(
                    onClick = {
                        runBlocking { instantGame.saveOnDb() } // Salva la partita nel database
                        navController.navigate("Home") // Torna alla schermata Home
                    },
                    enabled = !isGameFinished.value
                ) {
                    Text(text = "Finish and Return Home")
                }
            }
        }
    }
}

//TODO Risolvere i pulsanti che non sono cliccabili a fine partita e i tentativi che non funzionano