package com.example.numbergame.screens.record

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

    var selectedTab by remember { mutableStateOf("number") } // 숫자, 카드, 사칙연산, 반응속도
    var selectedOperation by remember { mutableStateOf<String?>(null) } // 사칙연산 연산 선택

    // 게임별 최대 난이도 정의
    val maxDifficulty = when (selectedTab) {
        "number" -> 4
        "card" -> 4
        "four_basic" -> 3
        else -> 1 // reaction 포함
    }
    val difficulties = (1..maxDifficulty).toList()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("🏆 최고 기록", fontSize = 26.sp)
        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 게임 탭
        Row(modifier = Modifier.fillMaxWidth()) {
            listOf(
                "number" to "숫자 맞히기",
                "card" to "카드 맞히기",
                "four_basic" to "사칙연산",
                "reaction" to "반응 속도"
            ).forEach { (type, label) ->
                Button(
                    onClick = {
                        selectedTab = type
                        selectedOperation = null // 사칙연산 선택 초기화
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedTab == type)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.secondary
                    ),
                    modifier = Modifier.weight(1f)
                ) { Text(label) }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 사칙연산 하위 연산 탭
        if (selectedTab == "four_basic") {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                listOf("+","-","×","÷").forEach { op ->
                    Button(
                        onClick = { selectedOperation = op },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedOperation == op)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.secondary
                        )
                    ) { Text(op, fontSize = 20.sp) }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // 🔹 난이도별 기록 표시
        if (selectedTab == "reaction") {
            // 반응속도는 난이도 구분 없이 최고 기록만 표시
            val recordFlow = RecordManager.getRecord(context, "reaction", 0)
            val record by recordFlow.collectAsState(initial = null)

            Card(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("반응 속도 테스트")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(record?.let { "최고 기록: ${it}초" } ?: "기록 없음")
                }
            }
        } else {
            // 기존 코드: 숫자/카드/사칙연산
            difficulties.forEach { difficulty ->
                val recordFlow = if (selectedTab == "four_basic" && selectedOperation != null) {
                    RecordManager.getRecord(context, selectedTab, difficulty, selectedOperation)
                } else {
                    RecordManager.getRecord(context, selectedTab, difficulty)
                }
                val record by recordFlow.collectAsState(initial = null)

                Card(modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("난이도 $difficulty")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(record?.let { "최고 기록: ${it}초" } ?: "기록 없음")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 🔹 기록 초기화
        Button(onClick = { scope.launch { RecordManager.resetAll(context) } }) {
            Text("기록 초기화")
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 🔹 메인 화면
        Button(onClick = { navController.navigate("main") { popUpTo("main") { inclusive = true } } }) {
            Text("메인 화면")
        }
    }
}