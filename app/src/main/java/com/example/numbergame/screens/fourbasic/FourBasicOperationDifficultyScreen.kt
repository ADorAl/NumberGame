package com.example.numbergame.screens.fourbasic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.numbergame.screens.GameMenuButton

@Composable
fun FourBasicOperationDifficultyScreen(
    navController: NavController,
    operation: String
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF101820))
            .padding(24.dp)
    ) {

        // 🔥 ⬅ 뒤로가기 버튼 (왼쪽 위)
        Button(
            onClick = { navController.popBackStack() },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF263238)
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Text("⬅ 뒤로가기", color = Color.White)
        }

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "⚔️ $operation 연산 선택",
                fontSize = 28.sp,
                color = Color.Yellow
            )

            Spacer(modifier = Modifier.height(40.dp))

            listOf(1, 2, 3).forEach { difficulty ->

                GameMenuButton(
                    text = "STAGE $difficulty",
                    color = when (difficulty) {
                        1 -> Color(0xFF4CAF50)
                        2 -> Color(0xFFFF9800)
                        else -> Color(0xFFF44336)
                    }
                ) {
                    navController.navigate(
                        "four_basic_operation/$operation/$difficulty"
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}