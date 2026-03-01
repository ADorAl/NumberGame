package com.example.numbergame.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay

@Composable
fun GameScreen(navController: NavController, difficulty: Int, context: Context) {
    val gridSize = difficulty + 2
    val totalCount = gridSize * gridSize

    var currentNumber by remember { mutableStateOf(1) }
    var numbers by remember { mutableStateOf((1..totalCount).shuffled()) }
    var missCount by remember { mutableStateOf(0) }
    val maxLives = 3

    var wrongIndex by remember { mutableStateOf<Int?>(null) }

    val startTime = remember { System.currentTimeMillis() }
    var elapsedSeconds by remember { mutableStateOf(0.0) }

    // 시간 업데이트
    LaunchedEffect(Unit) {
        while (true) {
            elapsedSeconds = (System.currentTimeMillis() - startTime) / 1000.0
            delay(10)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "현재 숫자: $currentNumber", fontSize = 22.sp)
        Text(text = "경과 시간: ${String.format("%.3f", elapsedSeconds)}초", fontSize = 18.sp)

        // 목숨 표시
        Row(horizontalArrangement = Arrangement.Center) {
            repeat(maxLives - missCount) {
                Text("❤️", fontSize = 22.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ✅ 빈틈 없는 n×n 격자
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val density = LocalDensity.current
            val cellSizePx = minOf(
                constraints.maxWidth / gridSize,
                constraints.maxHeight / gridSize
            )
            val cellSizeDp = with(density) { cellSizePx.toDp() }
            val fontSize = (cellSizePx / 8).sp // 버튼 크기에 비례한 글자 크기

            LazyVerticalGrid(
                columns = GridCells.Fixed(gridSize),
                modifier = Modifier.fillMaxSize(),
                userScrollEnabled = false
            ) {
                items(numbers.size) { index ->
                    val value = numbers[index]

                    val buttonColor = when {
                        value == -1 -> ButtonDefaults.buttonColors(containerColor = Color.Green)
                        wrongIndex == index -> ButtonDefaults.buttonColors(containerColor = Color.Red)
                        else -> ButtonDefaults.buttonColors()
                    }

                    Box(
                        modifier = Modifier
                            .size(cellSizeDp)
                            .padding(1.dp)
                    ) {
                        // 버튼 자체
                        Button(
                            onClick = {
                                if (value == currentNumber) {
                                    numbers = numbers.toMutableList().also { it[index] = -1 }
                                    currentNumber++

                                    if (currentNumber > totalCount) {
                                        val elapsed = (System.currentTimeMillis() - startTime) / 1000.0
                                        navController.navigate("success/$difficulty?time=$elapsed")
                                    }
                                } else {
                                    wrongIndex = index
                                    missCount++
                                    if (missCount >= maxLives) {
                                        navController.navigate("fail/$difficulty")
                                    }
                                }
                            },
                            modifier = Modifier.matchParentSize(), // ✅ 버튼이 Box 전체 차지
                            colors = buttonColor,
                            shape = RoundedCornerShape(0.dp) // ✅ 네모 모양 강제
                        ) { }

                        // 버튼 위에 텍스트 겹치기
                        if (value != -1) {
                            Text(
                                "$value",
                                fontSize = fontSize,
                                maxLines = 1,
                                softWrap = false,
                                textAlign = TextAlign.Center,
                                color = Color.White, // ✅ 글자 색을 흰색으로 변경
                                modifier = Modifier.align(Alignment.Center) // ✅ Box 안에서 중앙 배치
                            )
                        }
                    }

                    }

                }
            }
        }


    // 틀린 버튼 깜빡임 처리
    LaunchedEffect(wrongIndex) {
        if (wrongIndex != null) {
            delay(200)
            wrongIndex = null
        }
    }
}
