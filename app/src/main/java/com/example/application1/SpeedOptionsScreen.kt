package com.example.application1

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SpeedOptionsScreen(
    onModeSelected: (GameMode) -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8BBD0)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Choose Speed Mode", fontSize = 24.sp, color = Color.Black)

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Slow Mode",
            fontSize = 20.sp,
            modifier = Modifier
                .clickable { onModeSelected(GameMode.BUTTONS) }
                .padding(16.dp)
                .background(Color.White)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Fast Mode",
            fontSize = 20.sp,
            modifier = Modifier
                .clickable { onModeSelected(GameMode.BUTTONS_FAST) }
                .padding(16.dp)
                .background(Color.White)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Back",
            fontSize = 16.sp,
            modifier = Modifier
                .clickable { onBack() }
                .padding(12.dp)
        )
    }
}
