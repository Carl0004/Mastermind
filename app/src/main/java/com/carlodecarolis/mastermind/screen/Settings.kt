package com.carlodecarolis.mastermind.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.carlodecarolis.mastermind.logic.MyViewModel
import com.carlodecarolis.mastermind.ui.theme.Black200
import com.carlodecarolis.mastermind.ui.theme.W
import com.carlodecarolis.mastermind.ui.theme.ocra

@Composable
fun Settings(vm: MyViewModel, navController: NavController) {
    var selectedDifficulty by remember { mutableStateOf(vm.instantGame.difficulty) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Settings",
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            color = W
        )

        Text(
            text = "Difficulty:",
            color = W,
            fontSize = 20.sp
        )

        val difficultyOptions = listOf(
            Difficulty.Easy,
            Difficulty.Normal
        )

        difficultyOptions.forEach { difficulty ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (selectedDifficulty == difficulty),
                        onClick = {
                            selectedDifficulty = difficulty
                        }
                    )
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (selectedDifficulty == difficulty),
                    onClick = null
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = difficulty.name,
                    color = W,
                    fontSize = 20.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        // Pulsante "Salva" per confermare la selezione della difficoltà
        Button(
            onClick = {
                vm.instantGame.difficulty = selectedDifficulty
                navController.popBackStack()
            },
            modifier = Modifier
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ocra)
        ) {
            Text(
                text = "Save",
                color = Black200,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

enum class Difficulty {
    Easy,
    Normal
}