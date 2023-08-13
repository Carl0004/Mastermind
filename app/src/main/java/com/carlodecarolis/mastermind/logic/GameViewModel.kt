package com.carlodecarolis.mastermind.logic

import com.carlodecarolis.mastermind.logic.MyState.*
import androidx.compose.runtime.mutableStateOf
import com.carlodecarolis.mastermind.db.Repository

class GameViewModel(instantGame: InstantGame, repository: Repository) {
    var instantGame = instantGame
    var state = mutableStateOf(Init)
    var n = 0

    //Funzioni di prova
    fun init(){
        state.value = Init
        instantGame.newMatch()
    }
    fun new(){
        state.value = NewGame
    }

    fun hi(){
        state.value = History
    }
}