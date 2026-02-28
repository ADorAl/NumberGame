package com.example.numbergame

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.*
import com.example.numbergame.screens.DifficultyScreen
import com.example.numbergame.screens.FailScreen
import com.example.numbergame.screens.GameScreen
import com.example.numbergame.screens.HintDifficultyScreen
import com.example.numbergame.screens.HintGameScreen
import com.example.numbergame.screens.HintSuccessScreen
import com.example.numbergame.screens.MainScreen
import com.example.numbergame.screens.SuccessScreen
import com.example.numbergame.ui.theme.NumberGameTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NumberGameTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "main"
                ) {
                    // 메인 화면
                    composable("main") { MainScreen(navController) }

                    // 일반 모드
                    composable("difficulty") { DifficultyScreen(navController) }
                    composable("game/{difficulty}") { backStackEntry ->
                        val difficulty = backStackEntry.arguments?.getString("difficulty")?.toInt() ?: 1
                        GameScreen(navController, difficulty, this@MainActivity)
                    }
                    composable("success/{difficulty}?time={time}") { backStackEntry ->
                        val difficulty = backStackEntry.arguments?.getString("difficulty")?.toInt() ?: 1
                        val elapsedTime = backStackEntry.arguments?.getString("time")?.toDoubleOrNull()
                        SuccessScreen(navController, difficulty, this@MainActivity, elapsedTime)
                    }
                    composable("fail/{difficulty}") { backStackEntry ->
                        val difficulty = backStackEntry.arguments?.getString("difficulty")?.toInt() ?: 1
                        FailScreen(navController, difficulty)
                    }

                    // 힌트 모드
                    composable("hintDifficulty") { HintDifficultyScreen(navController) }
                    composable("hintGame/{difficulty}") { backStackEntry ->
                        val difficulty = backStackEntry.arguments?.getString("difficulty")?.toInt() ?: 1
                        HintGameScreen(navController, difficulty)
                    }
                    composable("hintSuccess?time={time}") { backStackEntry ->
                        val elapsedTime = backStackEntry.arguments?.getString("time")?.toDoubleOrNull() ?: 0.0
                        HintSuccessScreen(navController, elapsedTime)
                    }
                }
            }
        }
    }
}




//@Composable
//fun MainScreen(navController: NavController) {
//    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center,
//        modifier = Modifier.fillMaxSize()
//    ) {
//        Button(onClick = { navController.navigate("difficulty") }) {
//            Text("일반 모드 시작")
//        }
//        Spacer(modifier = Modifier.height(16.dp))
//        Button(onClick = { navController.navigate("hintDifficulty") }) {
//            Text("힌트 모드 시작")
//        }
//    }
//}





//@Composable
//fun DifficultyScreen(navController: NavController) {
//    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center,
//        modifier = Modifier.fillMaxSize()
//    ) {
//        for (i in 1..4) {
//            Button(onClick = { navController.navigate("game/$i") }) {
//                Text("난이도 $i")
//            }
//        }
//    }
//}

//@Composable
//fun GameScreen(navController: NavController, difficulty: Int, context: Context) {
//    val gridSize = difficulty + 2
//    val totalCount = gridSize * gridSize
//
//    var currentNumber by remember { mutableStateOf(1) }
//    var numbers by remember { mutableStateOf((1..totalCount).shuffled()) }
//    var missCount by remember { mutableStateOf(0) }
//    val maxLives = 3
//
//    // ❗ 틀린 버튼 깜빡임 상태 추가
//    var wrongIndex by remember { mutableStateOf<Int?>(null) }
//
//    val startTime = remember { System.currentTimeMillis() }
//    var elapsedSeconds by remember { mutableStateOf(0.0) }
//
//    // 🔹 실시간 시간 업데이트
//    LaunchedEffect(Unit) {
//        while (true) {
//            elapsedSeconds = (System.currentTimeMillis() - startTime) / 1000.0
//            delay(10)
//        }
//    }
//
//    Column(
//        modifier = Modifier.fillMaxSize(),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Spacer(modifier = Modifier.height(16.dp))
//        Text(text = "현재 숫자: $currentNumber", fontSize = 22.sp)
//        Text(text = "경과 시간: ${String.format("%.3f", elapsedSeconds)}초", fontSize = 18.sp)
//
//        // 목숨 표시
//        Row(horizontalArrangement = Arrangement.Center) {
//            repeat(maxLives - missCount) {
//                Text("❤️", fontSize = 22.sp)
//            }
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
//            val density = LocalDensity.current
//            val cellSizePx = minOf(
//                constraints.maxWidth / gridSize,
//                constraints.maxHeight / gridSize
//            )
//            val cellSizeDp = with(density) { cellSizePx.toDp() }
//            val fontSize = (cellSizePx / 4).sp
//
//            LazyVerticalGrid(
//                columns = GridCells.Fixed(gridSize),
//                modifier = Modifier.fillMaxSize(),
//                userScrollEnabled = false
//            ) {
//                items(numbers.size) { index ->
//                    val value = numbers[index]
//
//                    Box(
//                        modifier = Modifier
//                            .size(cellSizeDp)
//                            .padding(2.dp)
//                    ) {
//                        val buttonColor = when {
//                            value == -1 -> ButtonDefaults.buttonColors(containerColor = Color.Green) // ✅ 맞춘 버튼만 초록색
//                            wrongIndex == index -> ButtonDefaults.buttonColors(containerColor = Color.Red) // 틀린 버튼 빨간색
//                            else -> ButtonDefaults.buttonColors() // 기본 색상
//                        }
//
//
//                        Button(
//                            onClick = {
//                                if (value == currentNumber) {
//                                    numbers = numbers.toMutableList().also { it[index] = -1 }
//                                    currentNumber++
//
//                                    if (currentNumber > totalCount) {
//                                        val elapsed = (System.currentTimeMillis() - startTime) / 1000.0
//                                        navController.navigate("success/$difficulty?time=$elapsed")
//                                    }
//                                } else {
//                                    wrongIndex = index
//                                    missCount++
//                                    if (missCount >= maxLives) {
//                                        navController.navigate("fail/$difficulty")
//                                    }
//                                }
//                            },
//                            modifier = Modifier.fillMaxSize(),
//                            colors = buttonColor
//                        ) {
//                            if (value != -1) {
//                                Text(
//                                    "$value",
//                                    fontSize = fontSize,
//                                    maxLines = 1,
//                                    softWrap = false,
//                                    overflow = TextOverflow.Clip
//                                )
//                            }
//                        }
//
//                    }
//                }
//            }
//        }
//    }
//
//    // ❗ 틀린 버튼 깜빡임 처리 (컴포저블 본문 맨 아래)
//    LaunchedEffect(wrongIndex) {
//        if (wrongIndex != null) {
//            delay(200)
//            wrongIndex = null
//        }
//    }
//}





//@Composable
//fun SuccessScreen(
//    navController: NavController,
//    difficulty: Int,
//    context: Context,
//    elapsedTime: Double?
//) {
//    val records = remember { mutableStateListOf<Double>() }
//
//    // 🔹 기존 기록 불러오기
//    LaunchedEffect(Unit) {
//        records.clear()
//        records.addAll(getRecords(context, difficulty))
//    }
//
//    // 🔹 새로운 기록 저장
//    LaunchedEffect(elapsedTime) {
//        if (elapsedTime != null) {
//            saveRecord(context, difficulty, elapsedTime) // ✅ 단일 기록 저장
//            records.clear()
//            records.addAll(getRecords(context, difficulty)) // 다시 불러와서 최신화
//        }
//    }
//
//    val bestScore = records.minOrNull()
//
//    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center,
//        modifier = Modifier.fillMaxSize()
//    ) {
//        Text("성공! (이번 기록: ${elapsedTime?.let { String.format("%.3f", it) } ?: "-"}초)")
//        Spacer(modifier = Modifier.height(16.dp))
//
//        bestScore?.let {
//            Text("Best Score: ${String.format("%.3f", it)}초")
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//        Text("Top 10 기록:")
//        records.forEachIndexed { index, time ->
//            val isNew = elapsedTime?.let { Math.abs(it - time) < 0.001 } ?: false
//            Text("${index + 1}등: ${String.format("%.3f", time)}초 ${if (isNew) "new!" else ""}")
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//        Button(onClick = { navController.navigate("difficulty") }) {
//            Text("난이도 선택으로")
//        }
//
//        if (difficulty < 4) {
//            Button(onClick = { navController.navigate("game/${difficulty + 1}") }) {
//                Text("다음 난이도")
//            }
//        }
//    }
//}



//@Composable
//fun FailScreen(navController: NavController, difficulty: Int) {
//    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center,
//        modifier = Modifier.fillMaxSize()
//    ) {
//        Text("실패했습니다!")
//
//        Button(onClick = { navController.navigate("difficulty") }) {
//            Text("난이도 선택으로")
//        }
//
//        Button(onClick = { navController.navigate("game/$difficulty") }) {
//            Text("다시 시도")
//        }
//    }
//}

// 🔹 기록 저장 함수 (Double)
//fun saveRecord(context: Context, difficulty: Int, elapsed: Double) {
//    val prefs = context.getSharedPreferences("records", Context.MODE_PRIVATE)
//    val key = "difficulty_$difficulty"
//    val records = prefs.getStringSet(key, emptySet())!!.map { it.toDouble() }.toMutableList()
//
//    records.add(elapsed)
//    records.sort()
//    val top10 = records.take(10)
//
//    prefs.edit().putStringSet(key, top10.map { it.toString() }.toSet()).apply()
//}

// 🔹 기록 불러오기 함수 (Double)
//fun getRecords(context: Context, difficulty: Int): List<Double> {
//    val prefs = context.getSharedPreferences("records", Context.MODE_PRIVATE)
//    val key = "difficulty_$difficulty"
//    return prefs.getStringSet(key, emptySet())!!.map { it.toDouble() }.sorted()
//}


//@Composable
//fun HintGameScreen(navController: NavController, difficulty: Int) {
//    val gridSize = difficulty + 2
//    val totalCount = gridSize * gridSize
//
//    var currentNumber by remember { mutableStateOf(1) }
//    var numbers by remember { mutableStateOf((1..totalCount).shuffled()) }
//    var wrongIndex by remember { mutableStateOf<Int?>(null) }
//
//    val startTime = remember { System.currentTimeMillis() }
//    var elapsedSeconds by remember { mutableStateOf(0.0) }
//
//    LaunchedEffect(Unit) {
//        while (true) {
//            elapsedSeconds = (System.currentTimeMillis() - startTime) / 1000.0
//            delay(10)
//        }
//    }
//
//    Column(
//        modifier = Modifier.fillMaxSize(),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Spacer(modifier = Modifier.height(16.dp))
//        Text(text = "힌트 모드", fontSize = 22.sp)
//        Text(text = "현재 숫자: $currentNumber", fontSize = 22.sp)
//        Text(text = "경과 시간: ${String.format("%.3f", elapsedSeconds)}초", fontSize = 18.sp)
//        Spacer(modifier = Modifier.height(16.dp))
//
//        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
//            val density = LocalDensity.current
//            val cellSizePx = minOf(
//                constraints.maxWidth / gridSize,
//                constraints.maxHeight / gridSize
//            )
//            val cellSizeDp = with(density) { cellSizePx.toDp() }
//            val fontSize = (cellSizePx / 5).sp
//
//            LazyVerticalGrid(
//                columns = GridCells.Fixed(gridSize),
//                modifier = Modifier.fillMaxSize(),
//                userScrollEnabled = false
//            ) {
//                items(numbers.size) { index ->
//                    val value = numbers[index]
//
//                    Box(
//                        modifier = Modifier
//                            .size(cellSizeDp)
//                            .padding(2.dp)
//                    ) {
//                        val buttonColor = when {
//                            value == currentNumber -> ButtonDefaults.buttonColors(
//                                containerColor = Color.Green
//                            )
//                            value == -1 -> ButtonDefaults.buttonColors(
//                                containerColor = Color.Gray
//                            )
//                            wrongIndex == index -> ButtonDefaults.buttonColors(
//                                containerColor = Color.Red
//                            )
//                            else -> ButtonDefaults.buttonColors()
//                        }
//
//                        Button(
//                            onClick = {
//                                if (value == currentNumber) {
//                                    numbers = numbers.toMutableList().also { it[index] = -1 }
//                                    currentNumber++
//
//                                    if (currentNumber > totalCount) {
//                                        val elapsed = (System.currentTimeMillis() - startTime) / 1000.0
//                                        navController.navigate("hintSuccess?time=$elapsed")
//                                    }
//                                } else {
//                                    wrongIndex = index
//                                }
//                            },
//                            modifier = Modifier.fillMaxSize(),
//                            colors = buttonColor
//                        ) {
//                            if (value != -1) {
//                                Text(
//                                    "$value",
//                                    fontSize = fontSize,
//                                    maxLines = 1,
//                                    softWrap = false,
//                                    overflow = TextOverflow.Clip
//                                )
//                            }
//                        }
//
//                    }
//                }
//            }
//        }
//    }
//}


//@Composable
//fun HintDifficultyScreen(navController: NavController) {
//    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center,
//        modifier = Modifier.fillMaxSize()
//    ) {
//        for (i in 1..4) {
//            Button(onClick = { navController.navigate("hintGame/$i") }) {
//                Text("힌트 모드 난이도 $i")
//            }
//        }
//    }
//}



//@Composable
//fun HintSuccessScreen(navController: NavController, elapsedTime: Double) {
//    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center,
//        modifier = Modifier.fillMaxSize()
//    ) {
//        Text("힌트 모드 클리어!", fontSize = 24.sp)
//        Spacer(modifier = Modifier.height(16.dp))
//        Text("걸린 시간: ${String.format("%.3f", elapsedTime)}초",
//            fontSize = 20.sp)
//
//        Spacer(modifier = Modifier.height(16.dp))
//        Button(onClick = { navController.navigate("main") }){
//            Text("메인 화면으로")
//        }
//    }
//}

