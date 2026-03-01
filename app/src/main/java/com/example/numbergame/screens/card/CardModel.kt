package com.example.numbergame.screens.card

data class CardModel(
    val id: Int,
    val value: Int,
    val isFlipped: Boolean = false,
    val isMatched: Boolean = false
)