package com.example.application1

import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay
import kotlin.random.Random
import androidx.compose.foundation.layout.BoxWithConstraints
import com.example.application1.R

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GameScreen()
        }
    }
}

@Composable
fun GameScreen() {
    val context = LocalContext.current
    var lives by remember { mutableStateOf(3) }
    var fairyLane by remember { mutableStateOf(1) }
    val raindrops = remember { mutableStateListOf<Raindrop>() }
    var isGameOver by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(android.graphics.Color.parseColor("#B3E5FC"))),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // לבבות
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            repeat(3) { index ->
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

        // אזור המשחק
        BoxWithConstraints(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            val screenHeightPx = maxHeight.value
            val fairyHeightPx = 120f
            val fairyBottomPaddingPx = 80f
            val fairyY = screenHeightPx - fairyHeightPx - fairyBottomPaddingPx

            val laneWidth = maxWidth / 3

            // טיפות גשם חדשות
            LaunchedEffect(isGameOver) {
                if (!isGameOver) {
                    while (true) {
                        delay(1000L)
                        val newDrop = Raindrop(Random.nextInt(0, 3), 0f)
                        raindrops.add(newDrop)
                    }
                }
            }

            // הזזת טיפות + בדיקת התנגשות
            LaunchedEffect(isGameOver) {
                if (!isGameOver) {
                    while (true) {
                        delay(100L)
                        for (drop in raindrops) {
                            drop.y += 10f
                        }

                        val hitDrop = raindrops.find {
                            it.lane == fairyLane && it.y in (fairyY - 30)..(fairyY + 30)
                        }

                        if (hitDrop != null) {
                            raindrops.remove(hitDrop)
                            lives--
                            ToastWithVibration(context)
                            if (lives <= 0) {
                                isGameOver = true
                            }
                        }

                        raindrops.removeAll { it.y > screenHeightPx }
                    }
                }
            }

            // ציור טיפות
            raindrops.forEach { drop ->
                Box(
                    modifier = Modifier
                        .offset(x = laneWidth * drop.lane, y = drop.y.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.raindrop),
                        contentDescription = "Raindrop",
                        modifier = Modifier.size(50.dp)
                    )
                }
            }

            // ציור הפיה
            if (!isGameOver) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.fairy_no_bg),
                        contentDescription = "Fairy",
                        modifier = Modifier
                            .padding(start = laneWidth * fairyLane, bottom = fairyBottomPaddingPx.dp)
                            .size(fairyHeightPx.dp)
                    )
                }
            }

            // מסך GAME OVER
            if (isGameOver) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xAA000000)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Game Over \uD83D\uDE35",
                            color = Color.White,
                            modifier = Modifier.padding(16.dp)
                        )
                        Button(onClick = {
                            lives = 3
                            raindrops.clear()
                            isGameOver = false
                        }) {
                            Text("NEW GAME")
                        }
                    }
                }
            }
        }

        // כפתורי שליטה
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
                    .clickable {
                        if (fairyLane > 0) fairyLane--
                    }
            )

            Image(
                painter = painterResource(id = R.drawable.arrow_left),
                contentDescription = "Left Arrow",
                modifier = Modifier
                    .size(64.dp)
                    .clickable {
                        if (fairyLane < 2) fairyLane++
                    }
            )
        }
    }
}

fun ToastWithVibration(context: android.content.Context) {
    android.widget.Toast.makeText(context, "Hit by a raindrop!", android.widget.Toast.LENGTH_SHORT).show()
    val vibrator = context.getSystemService(android.content.Context.VIBRATOR_SERVICE) as Vibrator
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
        vibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        vibrator.vibrate(300)
    }
}

data class Raindrop(var lane: Int, var y: Float)

@Preview(showBackground = true)
@Composable
fun GamePreview() {
    GameScreen()
}
