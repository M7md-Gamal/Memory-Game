package com.elkabsh.memorygame.memory_featur.presentation.memory_screen

sealed class MemoryEvent {
    data class CardClicked(val cardId: Int) : MemoryEvent()
    object ResetGame : MemoryEvent()
    object NextTheme : MemoryEvent()
    object AddPair: MemoryEvent()
    object ReducePair: MemoryEvent()
}