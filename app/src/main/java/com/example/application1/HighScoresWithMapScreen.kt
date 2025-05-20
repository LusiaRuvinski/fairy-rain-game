package com.example.application1

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HighScoresWithMapScreen(
    scores: List<HighScore>,
    selectedScore: HighScore?,
    onScoreSelected: (HighScore) -> Unit,
    onBack: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {


        LazyColumn(
            modifier = Modifier
                .weight(0.5f)
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            items(scores) { score ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable { onScoreSelected(score) }
                ) {
                    Text(text = "🏁 Distance: ${score.score}m")
                    Text(text = "🪙 Coins: ${score.coins}")
                    Text(text = "📍 Location: ${score.locationName}")
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }


        Box(
            modifier = Modifier
                .weight(0.5f)
                .fillMaxWidth()
        ) {
            selectedScore?.let {
                MapViewSection(lat = it.latitude, lon = it.longitude)
            }
        }

        Button(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(12.dp),
            onClick = onBack
        ) {
            Text("Back to Menu")
        }
    }
}
