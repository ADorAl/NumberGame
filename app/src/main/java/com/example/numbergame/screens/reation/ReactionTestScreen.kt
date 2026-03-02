package com.example.numbergame.screens.reaction

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.example.numbergame.data.RecordManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

enum class CellState { EMPTY, WARNING, DANGER }

@Composable
fun ReactionTestScreen(navController: NavController) {

    val gridSize = 5
    var playerPos by remember { mutableStateOf(2 to 2) } // 중앙 시작
    var elapsedTime by remember { mutableStateOf(0L) }
    var gameOver by remember { mutableStateOf(false) }
    var bestTime by remember { mutableStateOf(0L) }

    val scope = rememberCoroutineScope()
    val context = navController.context

    // 위험 지점 그리드
    var dangerGrid by remember { mutableStateOf(List(gridSize) { MutableList(gridSize) { CellState.EMPTY } }) }

    // 경과 시간 카운트
    LaunchedEffect(gameOver) {
        while (!gameOver) {
            delay(1000)
            elapsedTime++
        }
    }

    // 위험 지점 루프 (플레이어 움직임과 독립)
    LaunchedEffect(Unit) {
        delay(1000) // 게임 시작 1초 대기
        while (true) {
            if (!gameOver) {
                // 1~4개 랜덤 WARNING
                val dangerCount = Random.nextInt(1, 5)
                val newWarnings = mutableListOf<Pair<Int, Int>>()
                repeat(dangerCount) {
                    var rx: Int
                    var ry: Int
                    do {
                        rx = Random.nextInt(gridSize)
                        ry = Random.nextInt(gridSize)
                    } while ((rx to ry) == playerPos || newWarnings.contains(rx to ry))
                    newWarnings.add(rx to ry)
                }

                // WARNING 3초
                newWarnings.forEach { (x, y) -> dangerGrid[y][x] = CellState.WARNING }
                delay(2000)

                // DANGER 1초
                newWarnings.forEach { (x, y) -> dangerGrid[y][x] = CellState.DANGER }
                delay(1000)

                // 1초 동안 플레이어 위치 체크
                repeat(10) { step ->
                    if (playerPos in newWarnings) {
                        gameOver = true
                        bestTime = maxOf(bestTime, elapsedTime)

                        // 🔹 RecordManager에 최고 기록 저장
                        scope.launch {
                            RecordManager.saveRecord(context, "reaction", 0, bestTime.toInt())
                        }
                    }
                    delay(100) // 0.1초 단위 체크
                }

                // 위험칸 초기화
                dangerGrid = dangerGrid.map { row ->
                    row.map { if (it == CellState.WARNING || it == CellState.DANGER) CellState.EMPTY else it }.toMutableList()
                }
            } else {
                delay(500) // 게임오버 상태에서는 루프 잠시 대기
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("반응 속도 테스트", fontSize = 26.sp)
        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 맵
        Column {
            for (y in 0 until gridSize) {
                Row {
                    for (x in 0 until gridSize) {
                        val color = when {
                            playerPos.first == x && playerPos.second == y -> Color.Blue
                            dangerGrid[y][x] == CellState.WARNING -> Color(0xFFFFA500)
                            dangerGrid[y][x] == CellState.DANGER -> Color.Red
                            else -> Color.White
                        }
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .border(1.dp, Color.Black)
                                .background(color)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 🔹 조작판 (원형 느낌)
        Box(
            modifier = Modifier.size(200.dp),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = { if (playerPos.second > 0) playerPos = playerPos.first to (playerPos.second - 1) },
                modifier = Modifier.align(Alignment.TopCenter)
            ) { Text("▲") }

            Button(
                onClick = { if (playerPos.second < gridSize - 1) playerPos = playerPos.first to (playerPos.second + 1) },
                modifier = Modifier.align(Alignment.BottomCenter)
            ) { Text("▼") }

            Button(
                onClick = { if (playerPos.first > 0) playerPos = (playerPos.first - 1) to playerPos.second },
                modifier = Modifier.align(Alignment.CenterStart)
            ) { Text("◀") }

            Button(
                onClick = { if (playerPos.first < gridSize - 1) playerPos = (playerPos.first + 1) to playerPos.second },
                modifier = Modifier.align(Alignment.CenterEnd)
            ) { Text("▶") }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("버틴 시간: $elapsedTime 초", fontSize = 18.sp)
        Text("최고 기록: $bestTime 초", fontSize = 18.sp, color = Color.Magenta)

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            // 완전 리셋
            playerPos = 2 to 2
            elapsedTime = 0
            gameOver = false
            dangerGrid = List(gridSize) { MutableList(gridSize) { CellState.EMPTY } }
        }) { Text("리셋") }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("main") }) { Text("메인으로") }
    }
}