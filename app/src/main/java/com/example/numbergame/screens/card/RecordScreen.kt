package com.example.numbergame.screens.card

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.numbergame.data.RecordManager
import kotlinx.coroutines.launch

@Composable
fun RecordScreen(navController: NavController) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val difficulties = listOf(1, 2, 3, 4)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("🏆 최고 기록", fontSize = 26.sp)

        Spacer(modifier = Modifier.height(24.dp))

        difficulties.forEach { difficulty ->

            val recordFlow =
                RecordManager.getRecord(context, difficulty)

            val record by recordFlow.collectAsState(initial = null)

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Text("난이도 $difficulty")

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        record?.let { "최고 기록: ${it}초" }
                            ?: "기록 없음"
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            scope.launch {
                RecordManager.resetAll(context)
            }
        }) {
            Text("기록 초기화")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = {
            navController.navigate("main") {
                popUpTo("main") { inclusive = true }
            }
        }) {
            Text("메인 화면")
        }
    }
}