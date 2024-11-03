package com.elkabsh.memorygame.memory_featur.presentation.memory_screen.componants

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elkabsh.memorygame.memory_featur.domain.model.MemoryCard
import com.elkabsh.memorygame.memory_featur.presentation.memory_screen.MemoryState

@Composable
fun MemoryGameCard(
    card: MemoryCard,
    state: MemoryState,
    onClick:() -> Unit,
    modifier: Modifier = Modifier
) {
    if (card.isBackDisplayed) {
        val localDensity= LocalDensity.current
        var cardHeight by remember { mutableStateOf(0.dp) }
        var cardWidth by remember { mutableStateOf(0.dp) }

        Card(
            shape = RoundedCornerShape(12.dp),
            modifier = modifier,
            onClick = onClick,
            colors = CardDefaults.cardColors(
                containerColor = state.currentTheme.cardBaseColor
            ),
            elevation = CardDefaults.cardElevation(6.dp)

        ){
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .onGloballyPositioned { layoutCoordinates ->
                        val density = localDensity
                        cardHeight = with(density) { layoutCoordinates.size.height.toDp() }
                        cardWidth = with(density) { layoutCoordinates.size.width.toDp() }
                    }
            ){
                val cardAspectRatio = cardWidth / cardHeight
                val shouldUseFillWidth = cardAspectRatio > 0.66f

                Image(
                    painter = painterResource(id = state.currentTheme.cardBack),
                    contentDescription = "Card Back",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = if (shouldUseFillWidth) {
                        ContentScale.FillWidth
                    } else {
                        ContentScale.FillHeight
                    },
                    alignment = Alignment.Center
                )

                Text(
                    text ="?",
                    color = state.currentTheme.cardTextColor,
                    fontSize = 70.sp,
                    fontWeight = FontWeight.ExtraBold,
                    style = TextStyle(
                        shadow = Shadow(
                            color = Color.Black,
                            offset = Offset(1f, 0f),
                            blurRadius = 40f
                        )
                    )
                )
            }
                // Back of the card

        }

    } else {
        // Front of the card

        val borderModifier = if (card.isMatched) {
            modifier.border(4.dp, state.currentTheme.matchedOutlinedColor, RoundedCornerShape(12.dp))
        } else {
            modifier
        }

        OutlinedCard(
            shape = RoundedCornerShape(12.dp),
            modifier = borderModifier,
            onClick = onClick,
            colors = CardDefaults.outlinedCardColors(
                containerColor = state.currentTheme.cardFrontBaseColor
            ),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {

            Box(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                Image(
                    painter = painterResource(id = state.currentTheme.imageMap[card.value]!!),
                    contentDescription = "Card Front",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit,
                    alignment = Alignment.Center
                )
            }

        }

    }

}