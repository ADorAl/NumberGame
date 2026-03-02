package com.example.numbergame.screens.fourbasic

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.numbergame.data.RecordManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun FourBasicOperationScreen(
    navController: NavController,
    operation: String,
    difficulty: Int
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var timeLeft by remember { mutableStateOf(30) } // 예시 30초
    var answer by remember { mutableStateOf("") }

    // 문제 생성
    val problem = remember { generateProblem(operation, difficulty) }

    // 타이머
    LaunchedEffect(timeLeft) {
        if (timeLeft > 0) {
            delay(1000)
            timeLeft--
        } else {
            navController.navigate("four_basic_operation_fail/$operation/$difficulty")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("문제: ${problem.question}", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text("남은 시간: $timeLeft 초", fontSize = 20.sp)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = answer,
            onValueChange = { answer = it },
            label = { Text("정답 입력") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val isCorrect = answer.toIntOrNull() == problem.answer
            val usedTime = (30 - timeLeft) // 예시 계산

            scope.launch {
                RecordManager.saveRecord(context, "four_basic", difficulty, usedTime)
                if (isCorrect) {
                    navController.navigate("four_basic_operation_success/$operation/$difficulty/$usedTime")
                } else {
                    navController.navigate("four_basic_operation_fail/$operation/$difficulty")
                }
            }
        }) {
            Text("제출")
        }
    }
}

data class Problem(val question: String, val answer: Int)

fun generateProblem(operation: String, difficulty: Int): Problem {
    // TODO: 난이도별 조건 + 사칙연산 로직 구현
    return Problem("1 + 1 = ?", 2)
}