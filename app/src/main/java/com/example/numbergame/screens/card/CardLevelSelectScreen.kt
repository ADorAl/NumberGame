package com.example.numbergame.screens.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun CardLevelSelectScreen(
    navController: NavController,
    mode: String
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        (1..4).forEach { level ->
            Button(
                onClick = {
                    navController.navigate(
                        "card_game/$mode/$level"
                    )
                }
            ) {
                Text("${level}단계")
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}