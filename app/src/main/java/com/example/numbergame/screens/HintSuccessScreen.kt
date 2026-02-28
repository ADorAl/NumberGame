package com.example.numbergame.screens

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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun HintSuccessScreen(navController: NavController, elapsedTime: Double) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text("힌트 모드 클리어!", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text("걸린 시간: ${String.format("%.3f", elapsedTime)}초",
            fontSize = 20.sp)

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("main") }){
            Text("메인 화면으로")
        }
    }
}
