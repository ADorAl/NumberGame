package com.example.numbergame.screens.number

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

@Composable
fun DifficultyScreen(navController: NavController, gameType: String) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Button(
            onClick = { navController.navigate("main") }, // ✅ MainScreen으로 이동
            modifier = Modifier.padding(16.dp)
            ) {
        Text("메인으로 돌아가기")
        }

        Button(
            onClick = { navController.navigate("hint")}, //HintDifficultyScreen으로 이동
            modifier = Modifier.padding(16.dp)
        ) {
            Text("힌트게임모드 가기")
        }

        Text("난이도를 선택하세요", fontSize = 24.sp, color = Color(0xFF2E2E2E))
        Spacer(modifier = Modifier.height(24.dp))

        for (i in 1..4) {
            Button(
                onClick = {
                    navController.navigate("game/$gameType/$i")
                },
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
            ) {
                Text("난이도 $i", color = Color.White, fontSize = 18.sp)
            }
        }
    }
}

