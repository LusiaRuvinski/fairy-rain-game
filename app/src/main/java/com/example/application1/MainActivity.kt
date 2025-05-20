package com.example.application1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }
}

@Composable
fun App() {
    val context = LocalContext.current
    var selectedMode by remember { mutableStateOf<GameMode?>(null) }
    var showHighScores by remember { mutableStateOf(false) }
    var selectedScore by remember { mutableStateOf<HighScore?>(null) }
    var showSpeedOptions by remember { mutableStateOf(false) }

    when {
        selectedMode != null -> {
            GameScreen(
                gameMode = selectedMode!!,
                onBackToMenu = {
                    selectedMode = null
                    showHighScores = false
                    selectedScore = null
                    showSpeedOptions = false
                }
            )
        }

        selectedScore != null -> {
            HighScoresWithMapScreen(
                scores = HighScoreManager.loadHighScores(context),
                selectedScore = selectedScore!!,
                onScoreSelected = { score -> selectedScore = score },
                onBack = {
                    selectedScore = null
                    showHighScores = true
                }
            )
        }

        showHighScores -> {
            HighScoresScreen(
                onBackToMenu = { showHighScores = false },
                onScoreSelected = { score ->
                    selectedScore = score
                    showHighScores = false
                }
            )
        }

        showSpeedOptions -> {
            SpeedOptionsScreen(
                onModeSelected = { mode ->
                    selectedMode = mode
                    showSpeedOptions = false
                },
                onBack = {
                    showSpeedOptions = false
                }
            )
        }

        else -> {
            StartScreen(
                onModeSelected = { mode ->
                    if (mode == GameMode.BUTTONS) {
                        showSpeedOptions = true
                    } else {
                        selectedMode = mode
                    }
                },
                onHighScoresClicked = { showHighScores = true }
            )
        }
    }
}
