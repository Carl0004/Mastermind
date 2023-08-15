package com.carlodecarolis.mastermind.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.carlodecarolis.mastermind.db.Game
import com.carlodecarolis.mastermind.logic.GameViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun History(navController: NavController, gameViewModel: GameViewModel) {
    var gameHistoryList by remember { mutableStateOf<List<Game>>(emptyList()) }

    LaunchedEffect(Unit) {
        val history = withContext(Dispatchers.IO) {
            gameViewModel.getAllGameHistory()
        }
        gameHistoryList = history
    }
    LazyColumn {
        items(gameHistoryList) { gameHistory ->
            GameHistoryItemRow(gameHistory)
        }
    }
}

@Composable
fun GameHistoryItemRow(gameHistory: Game) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(4.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
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
    }
}

@Composable
private fun formatDate(timestamp: Long): String {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = timestamp
    return dateFormat.format(calendar.time)
}
