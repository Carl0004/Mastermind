package com.carlodecarolis.mastermind.logic.utils

data class Attempt(
    val colors: List<Options>,
    val feedback: List<Feedback>
)