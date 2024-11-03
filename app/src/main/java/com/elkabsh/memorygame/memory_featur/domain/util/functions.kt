package com.elkabsh.memorygame.memory_featur.domain.util

import com.elkabsh.memorygame.memory_featur.domain.model.MemoryCard

fun generateCardArray(num:Int): Array<MemoryCard>{
    val singles=1..num
    val doubles= singles+singles

    return doubles.shuffled().map { MemoryCard(it) }.toTypedArray()

}

fun main(){
    val singles=1..6
    val doubles= singles+singles
    val x=arrayOf(doubles.shuffled())
    x.forEach { print(it) }
}