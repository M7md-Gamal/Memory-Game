package com.elkabsh.memorygame.memory_featur.presentation.memory_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elkabsh.memorygame.memory_featur.domain.util.generateCardArray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MemoryViewModel: ViewModel() {
    private val _state = mutableStateOf(MemoryState())
    val state : State<MemoryState> = _state

    private var delayedCompareJob:Job?=null

    fun onEvent(event: MemoryEvent){
        when(event){
            is MemoryEvent.CardClicked -> {
                onCardClick(event.cardId)
            }
            is MemoryEvent.ResetGame -> {
                resetGame()
            }
            is MemoryEvent.NextTheme -> {
                _state.value = _state.value.copy(currentTheme = _state.value.currentTheme.getNextTheme())
            }
            is MemoryEvent.AddPair -> {
                increaseMatches()
                resetGame()
            }
            is MemoryEvent.ReducePair -> {
                decreaseMatches()
                resetGame()
            }
        }
    }

    private fun compareCards(first: Int?, second: Int?){
        val cards=_state.value.cards.copyOf()
        if (first!=null && second!=null){
            val card1=cards[first]
            val card2=cards[second]
            if (card1.value==card2.value){
                card1.isMatched=true
                card2.isMatched=true
                _state.value=_state.value.copy(
                    cards = cards,
                    pairMatched = _state.value.pairMatched+1
                )
            }
            else{
                cards[first].flip()
                cards[second].flip()
            }


        }
        resetCompareCards()

    }
    fun resetCompareCards(){
        if (_state.value.card2!=null){
            _state.value=_state.value.copy(card1=null,card2=null)
        }
    }

    private fun cancelPriviesJob(){
        val firstIndex=_state.value.card1
        val secondIndex=_state.value.card2
        if (delayedCompareJob!=null){
            delayedCompareJob!!.cancel()
            compareCards(firstIndex,secondIndex)
        }
    }


    private fun onCardClick(id:Int) {
        cancelPriviesJob()
        increaseClickCount()
        val cards=_state.value.cards
        if (cards[id].isBackDisplayed){
            delayedCompareJob=viewModelScope.launch(Dispatchers.IO) {
                flipCard(id)
                val firstIndex= _state.value.card1
                val secondIndex= _state.value.card2
                val bothCardsAreNotNull = firstIndex!=null && secondIndex!=null
                val cardsMatchSkipDelay=if (bothCardsAreNotNull){
                    cards[firstIndex].value==cards[secondIndex].value
                }else{false}
                if (!cardsMatchSkipDelay){
                    delay(2000)
                }
                compareCards(firstIndex,secondIndex)
            }
        }

    }

    private fun increaseClickCount() {
        _state.value = _state.value.copy(clickCount = _state.value.clickCount + 1)
    }

    private fun flipCard(id:Int){
        val cards=_state.value.cards.copyOf()
        cards[id].flip()
        val card2=_state.value.card1
        _state.value=_state.value.copy(
            cards = cards,
            card1 = id,
            card2 = card2
        )
    }

    private fun increaseMatches() {
        if (_state.value.pairCount <9){
            _state.value = _state.value.copy(pairCount = _state.value.pairCount + 1)
        }
    }

    private fun decreaseMatches() {
        if (_state.value.pairCount >2){
            _state.value = _state.value.copy(pairCount = _state.value.pairCount - 1)
        }
    }

    private fun resetGame(){
        _state.value = MemoryState(
            cards = generateCardArray(_state.value.pairCount),
            currentTheme = _state.value.currentTheme,
            pairCount = _state.value.pairCount
        )
    }

}