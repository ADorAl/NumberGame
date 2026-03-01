package com.example.numbergame.screens.card

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CardViewModel : ViewModel() {

    private val _cards = MutableStateFlow<List<CardModel>>(emptyList())
    val cards: StateFlow<List<CardModel>> = _cards.asStateFlow()

    private val _elapsedTime = MutableStateFlow(0.0)
    val elapsedTime: StateFlow<Double> = _elapsedTime.asStateFlow()

    private val _isFinished = MutableStateFlow(false)
    val isFinished: StateFlow<Boolean> = _isFinished.asStateFlow()

    private var firstSelectedIndex: Int? = null
    private var secondSelectedIndex: Int? = null
    private var startTime: Long = 0L

    private var rows = 0
    private var cols = 0
    private var isPracticeMode = false
    private var difficulty = 1

    fun startGame(difficulty: Int, practiceMode: Boolean) {
        this.difficulty = difficulty
        this.isPracticeMode = practiceMode

        rows = difficulty + 1
        cols = 4

        val total = rows * cols
        val pairCount = total / 2

        val values = (1..pairCount).flatMap { listOf(it, it) }.shuffled()

        _cards.value = values.mapIndexed { index, value ->
            CardModel(index, value)
        }

        firstSelectedIndex = null
        secondSelectedIndex = null
        _isFinished.value = false

        startTime = System.currentTimeMillis()
        startTimer()
    }

    private fun startTimer() {
        viewModelScope.launch {
            while (!_isFinished.value) {
                _elapsedTime.value =
                    (System.currentTimeMillis() - startTime) / 1000.0
                delay(10)
            }
        }
    }

    fun onCardClick(index: Int) {
        val currentCards = _cards.value.toMutableList()
        val clicked = currentCards[index]

        if (clicked.isFlipped || clicked.isMatched) return
        if (secondSelectedIndex != null) return

        currentCards[index] = clicked.copy(isFlipped = true)
        _cards.value = currentCards

        if (firstSelectedIndex == null) {
            firstSelectedIndex = index
        } else {
            secondSelectedIndex = index
            checkMatch()
        }
    }

    private fun checkMatch() {
        viewModelScope.launch {
            delay(500)

            val currentCards = _cards.value.toMutableList()
            val first = firstSelectedIndex!!
            val second = secondSelectedIndex!!

            if (currentCards[first].value == currentCards[second].value) {
                currentCards[first] = currentCards[first].copy(isMatched = true)
                currentCards[second] = currentCards[second].copy(isMatched = true)
            } else {
                currentCards[first] = currentCards[first].copy(isFlipped = false)
                currentCards[second] = currentCards[second].copy(isFlipped = false)
            }

            _cards.value = currentCards
            firstSelectedIndex = null
            secondSelectedIndex = null

            if (currentCards.all { it.isMatched }) {
                _isFinished.value = true

                if (!isPracticeMode) {
                    val finalTime = _elapsedTime.value
                    // TODO: RecordUtil.saveRecord("card_normal_$difficulty", finalTime)
                }
            }
        }
    }

    fun getGridInfo(): Pair<Int, Int> = rows to cols
}