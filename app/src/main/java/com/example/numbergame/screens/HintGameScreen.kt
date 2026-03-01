package com.example.numbergame.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay

@Composable
fun HintGameScreen(navController: NavController, difficulty: Int) {
    val gridSize = difficulty + 2
    val totalCount = gridSize * gridSize

    var currentNumber by remember { mutableStateOf(1) }
    var numbers by remember { mutableStateOf((1..totalCount).shuffled()) }
    var wrongIndex by remember { mutableStateOf<Int?>(null) }

    val startTime = remember { System.currentTimeMillis() }
    var elapsedSeconds by remember { mutableStateOf(0.0) }

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
        Text(text = "힌트 모드", fontSize = 22.sp)
        Text(text = "현재 숫자: $currentNumber", fontSize = 22.sp)
        Text(text = "경과 시간: ${String.format("%.3f", elapsedSeconds)}초", fontSize = 18.sp)
        Spacer(modifier = Modifier.height(16.dp))

        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val density = LocalDensity.current
            val cellSizePx = minOf(
                constraints.maxWidth / gridSize,
                constraints.maxHeight / gridSize
            )
            val cellSizeDp = with(density) { cellSizePx.toDp() }
            val fontSize = (cellSizePx / 5).sp

            LazyVerticalGrid(
                columns = GridCells.Fixed(gridSize),
                modifier = Modifier.fillMaxSize(),
                userScrollEnabled = false
            ) {
                items(numbers.size) { index ->
                    val value = numbers[index]

                    Box(
                        modifier = Modifier
                            .size(cellSizeDp)
                            .padding(2.dp)
                    ) {
                        val buttonColor = when {
                            value == currentNumber -> ButtonDefaults.buttonColors(
                                containerColor = Color.Green
                            )
                            value == -1 -> ButtonDefaults.buttonColors(
                                containerColor = Color.Gray
                            )
                            wrongIndex == index -> ButtonDefaults.buttonColors(
                                containerColor = Color.Red
                            )
                            else -> ButtonDefaults.buttonColors()
                        }

                        Button(
                            onClick = {
                                if (value == currentNumber) {
                                    numbers = numbers.toMutableList().also { it[index] = -1 }
                                    currentNumber++

                                    if (currentNumber > totalCount) {
                                        val elapsed = (System.currentTimeMillis() - startTime) / 1000.0
                                        navController.navigate("hintSuccess?time=$elapsed")
                                    }
                                } else {
                                    wrongIndex = index
                                }
                            },
                            modifier = Modifier.matchParentSize(), // ✅ 버튼이 Box 전체 차지
                            colors = buttonColor,
                            shape = RoundedCornerShape(0.dp) // ✅ 네모 모양 강제
                        ) {
                            if (value != -1) {
                                Text(
                                    "$value",
                                    fontSize = fontSize,
                                    maxLines = 1,
                                    softWrap = false,
                                    overflow = TextOverflow.Clip
                                )
                            }
                        }

                    }
                }
            }
        }
    }
}