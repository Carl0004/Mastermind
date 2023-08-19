package com.carlodecarolis.mastermind.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.carlodecarolis.mastermind.ui.theme.Black200
import com.carlodecarolis.mastermind.ui.theme.ocra
import com.carlodecarolis.mastermind.ui.theme.white


@Composable
fun Home(navController: NavController){
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Mastermind",
            modifier = Modifier.padding(bottom = 16.dp),
            fontSize = 40.sp,
            color = white,
            fontWeight = FontWeight.Bold
        )
        HomeButton(text = "New Game", onClick = {navController.navigate("GameView")})
        Spacer(modifier = Modifier.height(16.dp))
        HomeButton(text = "Game History", onClick = {navController.navigate("History")})
    }
}

@Composable
fun HomeButton(text: String, onClick: () -> Unit){
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = ocra)
    ) {
        Text(text = text, color = Black200)
    }
}

