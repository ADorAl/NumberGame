package com.example.numbergame.screens

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun MainScreen(navController: NavController) {
    var clicked by remember { mutableStateOf(false) }

    // 버튼 이동 및 확대 애니메이션
    val scale by animateFloatAsState(targetValue = if (clicked) 1.5f else 1f, tween(300))
    val offsetX by animateDpAsState(targetValue = if (clicked) (-60).dp else 0.dp, tween(300))

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable { if (clicked) clicked = false } // 배경 클릭 시 원상복귀
    ) {
        // 화면 분할용 세로 선
        if (clicked) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val centerX = size.width / 2
                drawLine(
                    color = Color.Black,
                    start = androidx.compose.ui.geometry.Offset(centerX, 0f),
                    end = androidx.compose.ui.geometry.Offset(centerX, size.height),
                    strokeWidth = 4f
                )
            }
        }

        // 중앙 버튼
        Button(
            onClick = { clicked = !clicked },
            modifier = Modifier
                .align(Alignment.Center)
                .offset(x = offsetX)
                .scale(scale),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
        ) {
            if (!clicked) {
                Text("숫자 맞히기 🎯", fontSize = 20.sp, color = Color.White)
            } else {
                Text("🎯", fontSize = 28.sp, color = Color.White)
            }
        }

        // 오른쪽 버튼 영역
        if (clicked) {
            Column(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { navController.navigate("difficulty") },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
                ) {
                    Text("일반 모드", color = Color.White, fontSize = 18.sp)
                }
                Button(
                    onClick = { navController.navigate("hintDifficulty") },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF03DAC5))
                ) {
                    Text("연습 모드", color = Color.Black, fontSize = 18.sp)
                }
            }
        }
    }
}