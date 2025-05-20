package com.example.application1

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable

@Composable
fun HighScoresScreen(
    onBackToMenu: () -> Unit,
    onScoreSelected: (HighScore) -> Unit
)
 {
    val context = LocalContext.current
    val highScores = remember { HighScoreManager.loadHighScores(context) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Top 10 High Scores", modifier = Modifier.padding(8.dp))

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            items(highScores) { score ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onScoreSelected(score)}
                        .padding(8.dp)
                ) {
                    Text("🏁 Distance: ${score.score}m")
                    Text("🪙 Coins: ${score.coins}")
                    Text(text = "📍 Location: ${score.locationName}")
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }

        Button(onClick = onBackToMenu) {
            Text("Back to Menu")
        }
    }
}



