package com.example.numbergame.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.numbergame.data.getRecords
import com.example.numbergame.data.saveRecord
import kotlin.math.abs

@Composable
fun NumberSuccessScreen(
    navController: NavController,
    difficulty: Int,
    elapsedTime: Double?
) {

    val context = LocalContext.current   // ✅ 여기서 가져오기

    val records = remember { mutableStateListOf<Double>() }

    // 🔹 새로운 기록 저장 + 최신화
    LaunchedEffect(elapsedTime) {
        if (elapsedTime != null) {
            saveRecord(context, difficulty, elapsedTime)
            records.clear()
            records.addAll(getRecords(context, difficulty))
        }
    }

    val bestScore = records.minOrNull()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            "🎉 성공! (이번 기록: ${
                elapsedTime?.let { String.format("%.3f", it) } ?: "-"
            }초)",
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        bestScore?.let {
            Text(
                "Best Score: ${String.format("%.3f", it)}초",
                fontSize = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Top 10 기록:")

        records.forEachIndexed { index, time ->

            val isNew = elapsedTime?.let {
                abs(it - time) < 0.001
            } ?: false

            Text(
                "${index + 1}등: ${
                    String.format("%.3f", time)
                }초 ${if (isNew) "NEW!" else ""}"
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { navController.navigate("difficulty/number") },
            modifier = Modifier.fillMaxWidth(0.6f)
        ) {
            Text("난이도 선택으로")
        }

        if (difficulty < 4) {
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    navController.navigate("game/number/${difficulty + 1}")
                },
                modifier = Modifier.fillMaxWidth(0.6f)
            ) {
                Text("다음 난이도")
            }
        }
    }
}