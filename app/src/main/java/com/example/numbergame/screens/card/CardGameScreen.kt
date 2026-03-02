package com.example.numbergame.screens.card

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.example.numbergame.data.RecordManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.min

data class Card(
    val id: Int,
    val value: Int,
    val isFlipped: Boolean = false,
    val isMatched: Boolean = false
)

@Composable
fun CardGameScreen(navController: NavController, difficulty: Int) {

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val rowCount = difficulty + 1
    val columnCount = 4
    val totalCardCount = rowCount * columnCount
    val pairCount = totalCardCount / 2

    val timeLimit = when (difficulty) {
        1 -> 20
        2 -> 30
        3 -> 40
        else -> 50
    }

    var timeLeft by remember { mutableStateOf(timeLimit) }
    var cards by remember { mutableStateOf(generateCards(pairCount)) }
    var selectedCards by remember { mutableStateOf(listOf<Card>()) }

    // ⏳ 타이머
    LaunchedEffect(timeLeft) {
        if (timeLeft > 0) {
            delay(1000)
            timeLeft--
        } else {
            navController.navigate("card_fail/$difficulty")
        }
    }

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {

        val cardSize = min(
            maxWidth / columnCount - 12.dp,
            (maxHeight - 120.dp) / rowCount - 12.dp
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(16.dp))
            Text("남은 시간: $timeLeft 초", fontSize = 20.sp)
            Spacer(modifier = Modifier.height(16.dp))

            cards.chunked(columnCount).forEach { row ->
                Row {
                    row.forEach { card ->
                        FlipCard(
                            card = card,
                            size = cardSize,
                            onClick = {
                                if (!card.isFlipped && !card.isMatched && selectedCards.size < 2) {
                                    cards = cards.map { if (it.id == card.id) it.copy(isFlipped = true) else it }
                                    val newSelected = selectedCards + card.copy(isFlipped = true)
                                    selectedCards = newSelected

                                    if (newSelected.size == 2) {
                                        scope.launch {
                                            delay(700)

                                            if (newSelected[0].value == newSelected[1].value) {
                                                cards = cards.map {
                                                    if (it.id == newSelected[0].id || it.id == newSelected[1].id)
                                                        it.copy(isMatched = true)
                                                    else it
                                                }
                                            } else {
                                                cards = cards.map {
                                                    if (it.id == newSelected[0].id || it.id == newSelected[1].id)
                                                        it.copy(isFlipped = false)
                                                    else it
                                                }
                                            }

                                            selectedCards = emptyList()

                                            if (cards.all { it.isMatched }) {
                                                val usedTime = timeLimit - timeLeft

                                                scope.launch {
                                                    RecordManager.saveRecord(
                                                        context,
                                                        "card",
                                                        difficulty,
                                                        usedTime
                                                    )
                                                    navController.navigate("card_success/$difficulty/$usedTime")
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun FlipCard(card: Card, size: Dp, onClick: () -> Unit) {

    val rotation by animateFloatAsState(
        targetValue = if (card.isFlipped || card.isMatched) 180f else 0f,
        label = ""
    )

    Box(
        modifier = Modifier
            .padding(6.dp)
            .size(size)
            .clickable(enabled = !card.isMatched) { onClick() }
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 8 * density
            },
        contentAlignment = Alignment.Center
    ) {

        if (rotation <= 90f) {
            // 🔵 카드 뒷면
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF6200EE), RoundedCornerShape(8.dp))
            )
        } else {
            // 🟢 카드 앞면
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer { rotationY = 180f }
                    .background(
                        if (card.isMatched) Color.LightGray else Color.White,
                        RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(text = card.value.toString(), color = Color.Black, fontSize = 22.sp)
            }
        }
    }
}

fun generateCards(pairCount: Int): List<Card> {
    val numbers = (1..pairCount).flatMap { listOf(it, it) }.shuffled()
    return numbers.mapIndexed { index, value -> Card(id = index, value = value) }
}