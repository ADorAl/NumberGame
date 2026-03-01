package com.example.numbergame.screens.card

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
fun CardFailScreen(
    navController: NavController,
    difficulty: Int
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text("⏰ 시간 초과!", fontSize = 28.sp)

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            navController.navigate("card_game/$difficulty") {
                popUpTo("card_game/$difficulty") { inclusive = true }
            }
        }) {
            Text("다시 도전")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = {
            navController.navigate("difficulty/card") {
                popUpTo("main")
            }
        }) {
            Text("난이도 선택")
        }
    }
}