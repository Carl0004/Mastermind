package com.carlodecarolis.mastermind.db

data class Game(
    val id: Int,
    val secretCode: String,
    val guessSequence: String,
    val duration: Long,
    val date: String
)