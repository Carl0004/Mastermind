package com.carlodecarolis.mastermind.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


@Composable
fun Home(navController: NavController){
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Mastermind",
            modifier = Modifier.padding(bottom = 16.dp),
            fontSize = 30.sp,
        )
        HomeButton(text = "New Game", onClick = {navController.navigate("NewGame")})
        Spacer(modifier = Modifier.height(16.dp))
        HomeButton(text = "Continue", onClick = {navController.navigate("ContinueGame")})
        Spacer(modifier = Modifier.height(16.dp))
        HomeButton(text = "Game History", onClick = {navController.navigate("History")})
    }
}

@Composable
fun HomeButton(text: String, onClick: () -> Unit){
    Button(
        onClick = onClick,
    ) {
        Text(text = text)
    }
}

