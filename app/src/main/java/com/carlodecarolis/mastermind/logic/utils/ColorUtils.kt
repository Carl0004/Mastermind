package com.carlodecarolis.mastermind.logic.utils

import com.carlodecarolis.mastermind.R

object ColorUtils {

    fun getColorRes(color: String): Int {
        return when (color) {
            "W" -> R.color.white
            "R" -> R.color.cdnkncd
            "C" -> R.color.cyan
            "G" -> R.color.green
            "Y" -> R.color.yellow
            "P" -> R.color.purple
            "O" -> R.color.orange
            "B" -> R.color.blue
            else -> R.color.black // Colore di default
        }
    }
}