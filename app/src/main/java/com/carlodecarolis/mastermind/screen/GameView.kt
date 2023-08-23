package com.carlodecarolis.mastermind.screen

import android.content.res.Configuration
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.carlodecarolis.mastermind.db.DBMastermind
import com.carlodecarolis.mastermind.db.Repository
import com.carlodecarolis.mastermind.logic.InstantGame
import com.carlodecarolis.mastermind.logic.MyViewModel
import com.carlodecarolis.mastermind.logic.utils.Attempt
import com.carlodecarolis.mastermind.logic.utils.Feedback
import com.carlodecarolis.mastermind.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun GameView(navController: NavHostController, vm: MyViewModel) {
    val configuration = LocalConfiguration.current
    val feedbackTexts = remember { mutableStateListOf<String>() }
    when (configuration.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> {
            val selectedColors = remember { mutableStateListOf<String>("X", "X", "X", "X", "X") }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Button(
                        onClick = { navController.navigate("Home") },
                        colors = ButtonDefaults.buttonColors(ocra),
                        shape = RoundedCornerShape(15.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = null,
                            tint = Black200
                        )
                    }

                    Spacer(modifier = Modifier.weight(0.1f))
                    Text(
                        text = formatHour(vm.instantGame.duration.value),
                        color = W,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.weight(0.5f))

                // Area di gioco con tentativi
                GameArea(vm.instantGame, selectedColors)
                { i ->
                    selectedColors[i] = "X"
                }

                Spacer(modifier = Modifier.weight(0.5f))

                // Sezione di selezione dei colori
                ColorSelection(vm, selectedColors)
                { color ->
                    if (selectedColors.size < 8) {
                        for (i in 0 until selectedColors.size) {
                            if (selectedColors[i] == "X") {
                                selectedColors[i] = color
                                break
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(0.2f))

                // Pulsante Submit
                ButtonGuess(vm, selectedColors)
                { selectedColor ->
                    val reset = listOf("X", "X", "X", "X", "X")
                    vm.instantGame.attempt(selectedColor.joinToString(separator = ""))
                    selectedColors.clear()
                    selectedColors.addAll(reset)
                }
            }

            if (vm.instantGame.attempts.size >= 10) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.8f)) // Sfondo semitrasparente
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Codice Segreto:",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        Row(
                            modifier = Modifier.padding(bottom = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            for (color in vm.instantGame.secret.value) {
                                Canvas(
                                    modifier = Modifier.size(40.dp),
                                    onDraw = {
                                        drawCircle(
                                            color = getColorForCode(color.toString()), // Funzione per ottenere il colore corrispondente
                                            radius = size.minDimension / 2
                                        )
                                    }
                                )
                            }
                        }

                        // Aggiungi il pulsante "Home"
                        Button(
                            onClick = { navController.navigate("Home") },
                            colors = ButtonDefaults.buttonColors(ocra),
                            shape = RoundedCornerShape(15.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = null,
                                tint = Black200
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Home",
                                color = Black200
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun formatHour(timestamp: Long): String {
    val hourFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = timestamp
    return hourFormat.format(calendar.time)
}

fun getColorForCode(code: String): Color {
    return when (code) {
        "W" -> W
        "R" -> R
        "C" -> C
        "G" -> G
        "Y" -> Y
        "P" -> P
        "O" -> O
        "B" -> B
        else -> Black200// Colore di default o gestire altri casi
    }
}

@Composable
fun ButtonGuess(
    vm: MyViewModel,
    selectedColors: SnapshotStateList<String>,
    onClick: (SnapshotStateList<String>) -> Unit
) {
    Button(
        onClick = { onClick(selectedColors) },
        modifier = Modifier
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = ocra),
    ) {
        Text(
            text = "Submit",
            color = Black200
        )
    }
}


/*@Composable
fun GameArea(
    instantGame: InstantGame,
    selectedColors: List<String>,
    onClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        for (rowIndex in 0 until 10) {
            if (rowIndex == instantGame.attempts.size){
                 Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                        .border(BorderStroke(3.dp, Color.Black), shape = RoundedCornerShape(15.dp)) ,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    if (rowIndex < instantGame.attempts.size) {
                        for (i in 0 until 5) {
                            EmptyCircle(instantGame.attempts[rowIndex].guess.get(i).toString()){}
                        }
                    }else if (rowIndex == instantGame.attempts.size){
                        for (i in 0 until 5){
                            EmptyCircle(selectedColors[i], onClick = { onClick(i) })
                        }
                    }else{
                        for (i in 0 until 5) {
                            EmptyCircle(selectedColor = "X") {}
                        }
                    }
                }
            }else{
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    if (rowIndex < instantGame.attempts.size) {
                        for (i in 0 until 5) {
                            EmptyCircle(instantGame.attempts[rowIndex].guess.get(i).toString()){}
                        }
                    }else if (rowIndex == instantGame.attempts.size){
                        for (i in 0 until 5){
                            EmptyCircle(selectedColors[i], onClick = { onClick(i) })
                        }
                    }else{
                        for (i in 0 until 5)  {
                            EmptyCircle(selectedColor = "X"){}
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}*/

@Composable
fun GameArea(
    instantGame: InstantGame,
    selectedColors: List<String>,
    onClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        for (rowIndex in 0 until 10) {
            val feedbackRight = when {
                rowIndex < instantGame.attempts.size -> instantGame.attempts[rowIndex].rightNumRightPos
                else -> -1 // Segnaposto se il tentativo non è ancora stato effettuato
            }

            val feedbackWrong = when {
                rowIndex < instantGame.attempts.size -> instantGame.attempts[rowIndex].rightNumWrongPos
                else -> -1 // Segnaposto se il tentativo non è ancora stato effettuato
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween // Aggiunto questo
            ) {
                // Feedback a sinistra (colorato di rosso)
                Text(
                    text = if (feedbackWrong >= 0) feedbackWrong.toString() else "",
                    color = Color.Red,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                if (rowIndex < instantGame.attempts.size) {
                    for (i in 0 until 5) {
                        EmptyCircle(instantGame.attempts[rowIndex].guess.get(i).toString()){}
                    }
                } else if (rowIndex == instantGame.attempts.size) {
                    for (i in 0 until 5) {
                        EmptyCircle(selectedColors[i], onClick = { onClick(i) })
                    }
                } else {
                    for (i in 0 until 5) {
                        EmptyCircle(selectedColor = "X") {}
                    }
                }

                // Feedback a destra (colorato di blu)
                Text(
                    text = if (feedbackRight >= 0) feedbackRight.toString() else "",
                    color = Color.Blue,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}



@Composable
fun EmptyCircle(
    selectedColor: String?,
    onClick: () -> Unit
) {
    val colorValue = when (selectedColor) {
        "W" -> W
        "R" -> R
        "C" -> C
        "G" -> G
        "Y" -> Y
        "P" -> P
        "O" -> O
        "B" -> B
        else -> Color.Transparent
    }

    Canvas(
        modifier = Modifier
            .size(40.dp)
            .clickable { onClick() }
            .border(3.dp, Color.Black, shape = CircleShape)
    ){
        drawCircle(
            color = colorValue
        )
    }
}


@Composable
fun ColorSelection(
    vm: MyViewModel,
    selectedColors: MutableList<String>,
    onClick: (String) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        for (color in vm.instantGame.colorOptions) {
            ColorButton(
                color = color,
                onClick = { onClick(color) }
            )
        }
    }
}


@Composable
fun ColorButton(
    color: String,
    onClick: () -> Unit
) {
    val colorValue = when (color) {
        "W" -> W
        "R" -> R
        "C" -> C
        "G" -> G
        "Y" -> Y
        "P" -> P
        "O" -> O
        "B" -> B
        else -> Black200
    }

    Box(
        modifier = Modifier
            .size(40.dp)
            .padding(4.dp)
            .clickable {
                onClick()
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
                /*content = {
                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Black200
                        )
                    }
                }*/
            )
        }
    )
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val context = LocalContext.current
    val db = DBMastermind.getInstance(context)
    val repository = Repository(db.daoGameHistory())
    val instantGame = InstantGame(repository)
    val vm = MyViewModel(instantGame, repository)
    val navController = rememberNavController()

    MastermindTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Black200
        ) {
            GameView(vm = vm, navController = navController)
        }
    }
}