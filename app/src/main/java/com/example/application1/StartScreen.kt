package com.example.application1

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StartScreen(
    onModeSelected: (GameMode) -> Unit,
    onHighScoresClicked: () -> Unit)
{
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFB3E5FC)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Choose Game Mode", fontSize = 24.sp, color = Color.Black)

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            "Play with Buttons",
            fontSize = 20.sp,
            modifier = Modifier
                .clickable { onModeSelected(GameMode.BUTTONS) }
                .padding(16.dp)
                .background(Color.White)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Play with Sensor",
            fontSize = 20.sp,
            modifier = Modifier
                .clickable { onModeSelected(GameMode.SENSOR) }
                .padding(16.dp)
                .background(Color.White)
        )

        Spacer(modifier = Modifier.height(32.dp))


        Text(
            "View High Scores",
            fontSize = 20.sp,
            modifier = Modifier
                .clickable { onHighScoresClicked() }
                .padding(16.dp)
                .background(Color.Yellow)


        )
    }
}
