package com.example.numbergame.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun HintDifficultyScreen(navController: NavController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Button(
            onClick = { navController.navigate("main") }, // ✅ MainScreen으로 이동
            modifier = Modifier.padding(16.dp)
        ) {
            Text("메인으로 돌아가기")
        }

        for (i in 1..4) {
            Button(onClick = { navController.navigate("hintGame/$i") }) {
                Text("힌트 모드 난이도 $i")
            }
        }
    }
}
