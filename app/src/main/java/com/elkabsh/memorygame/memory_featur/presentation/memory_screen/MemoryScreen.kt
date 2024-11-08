package com.elkabsh.memorygame.memory_featur.presentation.memory_screen

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elkabsh.memorygame.memory_featur.presentation.memory_screen.componants.BuildButtons
import com.elkabsh.memorygame.memory_featur.presentation.memory_screen.componants.IconButton
import com.elkabsh.memorygame.memory_featur.presentation.memory_screen.componants.MemoryGameCard
import com.elkabsh.memorygame.memory_featur.presentation.util.calculateRowRanges

@Composable
fun MemoryScreen(
    modifier: Modifier = Modifier,
    viewModel: MemoryViewModel
){
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT
    val state = viewModel.state.value

    val backgroundImage = if(isPortrait) state.currentTheme.backgroundPortrait
    else state.currentTheme.backgroundLandscape

    Box(
        modifier = modifier.fillMaxSize()
    ){
        Image(
            painter = painterResource(id = backgroundImage),
            contentDescription = "Background Image",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )

        Row(
            modifier = Modifier.fillMaxSize()
        ){
            val rowRanges = calculateRowRanges(pairCount = state.pairCount, isPortrait = isPortrait)

            if(isPortrait){
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(top = 18.dp),
                    verticalArrangement =  Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    for(range in rowRanges){
                        BuildCardRow(
                            range = range,
                            numberOfRows = rowRanges.count(),
                            viewModel = viewModel,
                            modifier = Modifier.weight(1f),
                            isLastRow = rowRanges.last() == range
                        )
                    }
                    Row(
                        modifier = modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.06f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        BuildButtons(viewModel = viewModel, modifier = Modifier.weight(1f))
                    }
                }
            }else {
                Column(
                    modifier = modifier
                        .weight(4f)
                        .fillMaxSize()
                        .padding(top = 4.dp, bottom = 4.dp),
                    verticalArrangement =  Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    for(range in rowRanges){
                        BuildCardRow(
                            range = range,
                            numberOfRows = rowRanges.count(),
                            viewModel = viewModel,
                            modifier = Modifier.weight(1f),
                            isLastRow = rowRanges.last() == range
                        )
                    }
                }
                Column(
                    modifier = modifier
                        .weight(0.75f)
                        .fillMaxSize(),
                    verticalArrangement =  Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    BuildButtons(viewModel = viewModel, modifier = Modifier.weight(1f))
                }
            }
        }
    }
    if(state.pairCount == state.pairMatched){
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .border(
                        width = 4.dp,
                        shape = RoundedCornerShape(12.dp),
                        color = state.currentTheme.matchedOutlinedColor
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = state.currentTheme.cardFrontBaseColor
                )
            ){
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "You have won!\nScore: ${state.clickCount} clicks",
                        color = Color.Black,
                        fontSize = 30.sp,
                        lineHeight = 38.sp,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    IconButton(
                        onClick = { viewModel.onEvent(MemoryEvent.ResetGame) },
                        icon = Icons.Default.Refresh,
                        contentDescription = "Reset Game Button",
                        tint = state.currentTheme.iconColor,
                        modifier = modifier
                    )
                }
            }
        }
    }
}

@Composable
fun BuildCardRow(
    modifier: Modifier = Modifier,
    range: IntRange,
    numberOfRows: Int,
    viewModel: MemoryViewModel,
    isLastRow: Boolean = false
){
    val state = viewModel.state.value
    Box(modifier = modifier){
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .padding(8.dp)
                .fillMaxSize()
        ){
            for(cardId in range){
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ){
                    MemoryGameCard(
                        card = state.cards[cardId],
                        state = state,
                        modifier = Modifier
                            .padding(6.dp)
                            .fillMaxSize(),
                        onClick = {viewModel.onEvent(MemoryEvent.CardClicked(cardId))}
                    )
                }

            }
            val modulus = (state.pairCount * 2) % numberOfRows
            if(isLastRow && modulus != 0){
                val fillerCardNumber = numberOfRows - modulus
                val extraCards = 1..fillerCardNumber
                for(missingCard in extraCards){
                    Box(
                        Modifier
                            .weight(1f)
                            .fillMaxSize())
                }
            }
        }


    }
}

