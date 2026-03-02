package com.example.numbergame.screens.fourbasic

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun FourBasicOperationSuccessScreen(
    navController: NavController,
    operation: String,
    difficulty: Int,
    usedTime: Int
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("🎉 성공!", fontSize = 32.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text("연산: $operation", fontSize = 24.sp)
        Text("난이도: $difficulty", fontSize = 24.sp)
        Text("걸린 시간: $usedTime 초", fontSize = 24.sp)

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { navController.navigate("main") },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors()
        ) {
            Text("메인으로 돌아가기", fontSize = 20.sp)
        }
    }
}