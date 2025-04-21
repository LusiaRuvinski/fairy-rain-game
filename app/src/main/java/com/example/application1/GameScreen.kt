package com.example.application1

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun GameScreen() {
    val context = LocalContext.current
    var lives by remember { mutableStateOf(Constants.START_LIVES) }
    var fairyLane by remember { mutableStateOf(Constants.START_LANE) }
    val raindrops = remember { mutableStateListOf<Raindrop>() }
    var isGameOver by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(android.graphics.Color.parseColor(Constants.BACKGROUND_COLOR))),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // hearts
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            repeat(Constants.START_LIVES) { index ->
                if (index < lives) {
                    Image(
                        painter = painterResource(id = R.drawable.heart_full),
                        contentDescription = "Heart",
                        modifier = Modifier
                            .size(32.dp)
                            .padding(4.dp)
                    )
                }
            }
        }

        // Game area
        BoxWithConstraints(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            val screenHeightPx = maxHeight.value
            val fairyY = screenHeightPx - Constants.FAIRY_HEIGHT - Constants.FAIRY_BOTTOM_PADDING
            val laneWidth = maxWidth / 3

            // new drops
            LaunchedEffect(isGameOver) {
                if (!isGameOver) {
                    while (true) {
                        delay(Constants.DROP_INTERVAL_MS)
                        raindrops.add(Raindrop(Random.nextInt(0, 3), 0f))
                    }
                }
            }

            // move drops and detect collisions
            LaunchedEffect(isGameOver) {
                if (!isGameOver) {
                    while (true) {
                        delay(Constants.MOVE_INTERVAL_MS)
                        for (drop in raindrops) drop.y += Constants.DROP_SPEED

                        val hitDrop = raindrops.find {
                            it.lane == fairyLane && it.y in (fairyY - 30)..(fairyY + 30)
                        }

                        if (hitDrop != null) {
                            raindrops.remove(hitDrop)
                            lives--
                            ToastWithVibration(context)
                            if (lives <= 0) isGameOver = true
                        }

                        raindrops.removeAll { it.y > screenHeightPx }
                    }
                }
            }

            // draw drops
            raindrops.forEach { drop ->
                Box(
                    modifier = Modifier
                        .offset(x = laneWidth * drop.lane, y = drop.y.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.raindrop),
                        contentDescription = "Raindrop",
                        modifier = Modifier.size(Constants.DROP_SIZE.dp)
                    )
                }
            }

            // fairy
            if (!isGameOver) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.fairy_no_bg),
                        contentDescription = "Fairy",
                        modifier = Modifier
                            .padding(start = laneWidth * fairyLane, bottom = Constants.FAIRY_BOTTOM_PADDING.dp)
                            .size(Constants.FAIRY_HEIGHT.dp)
                    )
                }
            }

            // Game Over
            if (isGameOver) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xAA000000)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Game Over 😵",
                            color = Color.White,
                            modifier = Modifier.padding(16.dp)
                        )
                        Button(onClick = {
                            lives = Constants.START_LIVES
                            raindrops.clear()
                            isGameOver = false
                        }) {
                            Text("NEW GAME")
                        }
                    }
                }
            }
        }

        // arrows
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Image(
                painter = painterResource(id = R.drawable.arrow_right),
                contentDescription = "Right Arrow",
                modifier = Modifier
                    .size(64.dp)
                    .clickable { if (fairyLane > 0) fairyLane-- }
            )

            Image(
                painter = painterResource(id = R.drawable.arrow_left),
                contentDescription = "Left Arrow",
                modifier = Modifier
                    .size(64.dp)
                    .clickable { if (fairyLane < 2) fairyLane++ }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GamePreview() {
    GameScreen()
}
