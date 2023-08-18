package com.carlodecarolis.mastermind.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.carlodecarolis.mastermind.logic.GameViewModel
import com.carlodecarolis.mastermind.logic.InstantGame
import com.carlodecarolis.mastermind.logic.utils.Attempt
import com.carlodecarolis.mastermind.logic.utils.Feedback
import com.carlodecarolis.mastermind.logic.utils.Options
import com.carlodecarolis.mastermind.ui.theme.*


@Composable
fun GameView(
    navController: NavController,
    gameViewModel: GameViewModel
) {
    val instantGame = remember { InstantGame(gameViewModel) }

    val selectedColors = remember { mutableStateListOf<String>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Area di gioco con tentativi
        GameArea(instantGame.attempts)

        Spacer(modifier = Modifier.height(16.dp))

        // Sezione di selezione dei colori
        Spacer(modifier = Modifier.weight(1f)) // Spazio flessibile che occupa lo spazio rimanente
        ColorSelection(selectedColors, instantGame.colorOptions) { color ->
            if (selectedColors.size < 8) {
                selectedColors.add(color)
            }
        }

        Button(
            onClick = {
                if (selectedColors.size == 8) {
                    val optionsList = selectedColors.map { Options(it, isSelected = 1) }
                    instantGame.attempt(optionsList)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            enabled = selectedColors.size == 8
        ) {
            Text(text = "Submit")
        }
    }
}


@Composable
fun GameArea(attempts: List<Attempt>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        attempts.reversed().forEach { attempt ->
            AttemptRow(attempt)
        }
    }
}

@Composable
fun AttemptRow(attempt: Attempt) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        attempt.colors.forEach { color ->
            ColorCircle(
                color = color.color,
                isSelected = false, // Imposta isSelected a false per i cerchi nei tentativi
                onClick = {} // Non è necessario un onClick per i cerchi nei tentativi
            )
        }
        attempt.feedback.forEach { feedback ->
            FeedbackCircle(feedback)
        }
    }
}

@Composable
fun ColorCircle(
    color: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val colorValue = when (color) {
        "W" -> white
        "R" -> red
        "C" -> cyan
        "G" -> green
        "Y" -> yellow
        "P" -> purple
        "O" -> orange
        "B" -> blue
        else -> black // Colore di default
    }

    Box(
        modifier = Modifier
            .size(40.dp)
            .padding(4.dp)
            .background(color = colorValue)
            .border(3.dp, Color.Black, shape = CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(Color.White, CircleShape)
            )
        }
    }
}

@Composable
fun ColorSelection(
    selectedColors: MutableList<String>,
    colorOptions: List<String>,
    onColorSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        for (colorOption in colorOptions) {
            ColorButton(
                color = colorOption,
                isSelected = selectedColors.contains(colorOption),
                onColorSelected = { onColorSelected(colorOption) }
            )
        }
    }
}

@Composable
fun ColorButton(
    color: String,
    isSelected: Boolean,
    onColorSelected: () -> Unit
) {
    val colorValue = when (color) {
        "W" -> white
        "R" -> red
        "C" -> cyan
        "G" -> green
        "Y" -> yellow
        "P" -> purple
        "O" -> orange
        "B" -> blue
        else -> black // Colore di default
    }

    Box(
        modifier = Modifier
            .size(48.dp)
            .padding(4.dp)
            .clickable {
                onColorSelected()
            },
        contentAlignment = Alignment.Center,
        content = {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(
                        color = colorValue,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center,
                content = {
                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
            )
        }
    )
}

@Composable
fun FeedbackCircle(feedback: Feedback, modifier: Modifier = Modifier) {
    val circleColor = when (feedback) {
        Feedback.CORRECT_COLOR_WRONG_POSITION -> Color.Gray
        Feedback.CORRECT_COLOR_CORRECT_POSITION -> Color.Black
    }

    Box(
        modifier = modifier
            .size(16.dp)
            .background(circleColor, CircleShape)
    )
}