package com.carlodecarolis.mastermind.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.carlodecarolis.mastermind.db.Game
import com.carlodecarolis.mastermind.logic.GameViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun History(navController: NavController, gameViewModel: GameViewModel) {
    var gameHistoryList by remember { mutableStateOf<List<Game>>(emptyList()) }
    var selectedCount by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        val history = withContext(Dispatchers.IO) {
            gameViewModel.getAllGameHistory()
        }
        gameHistoryList = history.map { it.copy(isSelected = false) }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        LazyColumn {
            items(gameHistoryList) { gameHistory ->
                GameHistoryItemRow(
                    gameHistory = gameHistory,
                    gameViewModel = gameViewModel,
                    onSelectedChanged = { isSelected ->
                        if (isSelected) {
                            selectedCount++
                        } else {
                            selectedCount--
                        }
                    }
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            Text(
                text = "Selected: $selectedCount",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)  // non cancellare!!
            )

            Button(
                onClick = {
                    val selectedGames = gameHistoryList.filter { it.isSelected }
                    if (selectedGames.isNotEmpty()) {
                        runBlocking {
                            gameViewModel.deleteSelectedGames(selectedGames)
                        }
                        gameHistoryList = gameHistoryList.filterNot { it.isSelected }
                        selectedCount = 0
                    }
                }
            ) {
                Text(text = "Delete Selected")
            }
        }
    }
}

@Composable
fun GameHistoryItemRow(
    gameHistory: Game,
    gameViewModel: GameViewModel,
    onSelectedChanged: (Boolean) -> Unit
    ) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(4.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "ID: ${gameHistory.id}", fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Versione App: ${gameHistory.version}", fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Codice Segreto: ${gameHistory.secretCode}", fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Risultato: ${gameHistory.result}", fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Tentativi: ${gameHistory.attempts}", fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Durata Partita: ${gameHistory.duration} ms", fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Data Partita: ${formatDate(gameHistory.date)}", fontSize = 16.sp)
            }

            Checkbox(
                checked = gameHistory.isSelected,
                onCheckedChange = { isSelected ->
                    onSelectedChanged(isSelected)
                    gameHistory.isSelected = isSelected
                }
            )
        }
    }
}


@Composable
private fun formatDate(timestamp: Long): String {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = timestamp
    return dateFormat.format(calendar.time)
}
