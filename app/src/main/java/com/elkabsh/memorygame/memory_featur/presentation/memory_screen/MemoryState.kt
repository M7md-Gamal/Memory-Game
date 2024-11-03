package com.elkabsh.memorygame.memory_featur.presentation.memory_screen

import com.elkabsh.memorygame.memory_featur.domain.model.MemoryCard
import com.elkabsh.memorygame.memory_featur.domain.util.generateCardArray
import com.elkabsh.memorygame.ui.theme.HolidayTheme

data class MemoryState(
    val cards: Array<MemoryCard> = generateCardArray(6),
    val card1:Int?=null,
    val card2:Int?=null,
    val pairCount:Int=6,
    val pairMatched:Int=0,
    val clickCount:Int=0,
    val currentTheme:HolidayTheme= HolidayTheme.ThanksGivingTheme()
)