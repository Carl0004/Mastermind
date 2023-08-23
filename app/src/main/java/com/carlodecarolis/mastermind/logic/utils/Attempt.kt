package com.carlodecarolis.mastermind.logic.utils

data class Attempt(
    val guess : String,
    val rightNumRightPos : Int,
    val rightNumWrongPos: Int
)