package com.example.numbergame.screens

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.numbergame.data.getRecords
import com.example.numbergame.data.saveRecord
import kotlin.compareTo


@Composable
fun SuccessScreen(
    navController: NavController,
    difficulty: Int,
    context: Context,
    elapsedTime: Double?
) {
    val records = remember { mutableStateListOf<Double>() }

    // 🔹 기존 기록 불러오기
    LaunchedEffect(Unit) {
        records.clear()
        records.addAll(getRecords(context, difficulty))
    }

    // 🔹 새로운 기록 저장
    LaunchedEffect(elapsedTime) {
        if (elapsedTime != null) {
            saveRecord(context, difficulty, elapsedTime) // ✅ 단일 기록 저장
            records.clear()
            records.addAll(getRecords(context, difficulty)) // 다시 불러와서 최신화
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
        Text("🎉 성공! (이번 기록: ${elapsedTime?.let { String.format("%.3f", it) } ?: "-" }초)", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))
        bestScore?.let { Text("Best Score: ${String.format("%.3f", it)}초", fontSize = 20.sp) }


        Spacer(modifier = Modifier.height(16.dp))
        Text("Top 10 기록:")
        records.forEachIndexed { index, time ->
            val isNew = elapsedTime?.let { Math.abs(it - time) < 0.001 } ?: false
            Text("${index + 1}등: ${String.format("%.3f", time)}초 ${if (isNew) "new!" else ""}")
        }

        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = { navController.navigate("difficulty") }, modifier = Modifier.fillMaxWidth(0.6f)) {
            Text("난이도 선택으로")
        }
        if (difficulty < 4) {
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.navigate("game/${difficulty + 1}") }, modifier = Modifier.fillMaxWidth(0.6f)) {
                Text("다음 난이도")
            }
        }
    }
}
