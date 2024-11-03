package com.elkabsh.memorygame.memory_featur.domain.model
class MemoryCard(
    val value: Int,
    var isBackDisplayed: Boolean = true,
    var isMatched: Boolean = false
){
    fun flip(){
        isBackDisplayed = !isBackDisplayed
    }

}
