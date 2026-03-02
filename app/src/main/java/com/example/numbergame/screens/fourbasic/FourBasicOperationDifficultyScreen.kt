package com.example.numbergame.screens.fourbasic

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun FourBasicOperationDifficultyScreen(
    navController: NavController,
    operation: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("선택한 연산: $operation", fontSize = 28.sp)
        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf(1, 2, 3).forEach { difficulty ->
                Button(
                    onClick = {
                        navController.navigate("four_basic_operation/$operation/$difficulty")
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(60.dp)
                        .padding(horizontal = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = androidx.compose.ui.graphics.Color(0xFFFFC107))
                ) {
                    Text("난이도 $difficulty", fontSize = 20.sp)
                }
            }
        }
    }
}