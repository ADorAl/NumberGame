package com.example.numbergame.screens.main

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun MainScreen(navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Button(onClick = {
            navController.navigate("record")
        }) {
            Text("기록 보기")
        }

        // 🔹 숫자 맞히기 버튼
        Button(
            onClick = {
                navController.navigate("difficulty/number")
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "숫자 맞히기",
                    fontSize = 20.sp
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "🎯",
                    fontSize = 24.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // 🔹 카드 짝 맞추기 버튼
        Button(
            onClick = {
                navController.navigate("difficulty/card")
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4CAF50)
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "카드 짝 맞추기",
                    fontSize = 20.sp
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "🃏",
                    fontSize = 24.sp
                )
            }
        }
    }
}