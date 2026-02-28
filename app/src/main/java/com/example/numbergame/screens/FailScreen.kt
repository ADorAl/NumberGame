package com.example.numbergame.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun FailScreen(navController: NavController, difficulty: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text("실패했습니다!")

        Button(onClick = { navController.navigate("difficulty") }) {
            Text("난이도 선택으로")
        }

        Button(onClick = { navController.navigate("game/$difficulty") }) {
            Text("다시 시도")
        }
    }
}