package com.example.application1

import android.media.MediaPlayer
import android.Manifest
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Geocoder
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.delay
import java.util.Locale
import kotlin.random.Random

@Composable
fun GameScreen(gameMode: GameMode, onBackToMenu: () -> Unit) {
    val context = LocalContext.current
    val sensorManager = context.getSystemService(SensorManager::class.java)
    val accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    val permissionGranted = remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        permissionGranted.value = isGranted
    }

    LaunchedEffect(Unit) {
        if (!permissionGranted.value) {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    var lives by remember { mutableStateOf(Constants.START_LIVES) }
    var fairyLane by remember { mutableStateOf(Constants.START_LANE) }
    val raindrops = remember { mutableStateListOf<Raindrop>() }
    var isGameOver by remember { mutableStateOf(false) }
    val coins = remember { mutableStateListOf<Coin>() }
    var collectedCoins by remember { mutableStateOf(0) }
    var distance by remember { mutableStateOf(0) }
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    if (gameMode == GameMode.SENSOR && accelerometer != null) {
        var lastMoveTime by remember { mutableStateOf(0L) }

        DisposableEffect(Unit) {
            val sensorListener = object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent) {
                    val x = event.values[0]
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - lastMoveTime > 300) {
                        if (x < -2 && fairyLane > 0) {
                            fairyLane--
                        } else if (x > 2 && fairyLane < Constants.NUM_LANES - 1) {
                            fairyLane++
                        }
                        lastMoveTime = currentTime
                    }
                }
                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
            }
            sensorManager.registerListener(sensorListener, accelerometer, SensorManager.SENSOR_DELAY_GAME)
            onDispose { sensorManager.unregisterListener(sensorListener) }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(android.graphics.Color.parseColor(Constants.BACKGROUND_COLOR))),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(painter = painterResource(id = R.drawable.coin), contentDescription = "Coin", modifier = Modifier.size(24.dp))
                Text(": $collectedCoins", color = Color.Black, modifier = Modifier.padding(start = 4.dp, end = 16.dp))
                Text("Distance: $distance", color = Color.Black)
            }
            Row {
                repeat(Constants.START_LIVES) { index ->
                    if (index < lives) {
                        Image(
                            painter = painterResource(id = R.drawable.heart_full),
                            contentDescription = "Heart",
                            modifier = Modifier.size(32.dp).padding(4.dp)
                        )
                    }
                }
            }
        }

        BoxWithConstraints(
            modifier = Modifier.weight(1f).fillMaxWidth()
        ) {
            val screenHeightPx = maxHeight.value
            val fairyY = screenHeightPx - Constants.FAIRY_HEIGHT - Constants.FAIRY_BOTTOM_PADDING
            val laneWidth = maxWidth / Constants.NUM_LANES

            val moveInterval = Constants.MOVE_INTERVAL_MS


            val dropSpeed = when (gameMode) {
                GameMode.BUTTONS_SLOW -> Constants.DROP_SPEED * 1f
                GameMode.BUTTONS_FAST -> Constants.DROP_SPEED * 3f
                else -> Constants.DROP_SPEED * 1f
            }

            LaunchedEffect(isGameOver) {
                if (!isGameOver) {
                    while (true) {
                        delay(Constants.DROP_INTERVAL_MS)
                        raindrops.add(Raindrop(Random.nextInt(0, Constants.NUM_LANES), 0f))
                        if (Random.nextFloat() < 0.4f) {
                            val occupiedLanes = raindrops.map { it.lane }
                            val availableLanes = (0 until Constants.NUM_LANES).filter { it !in occupiedLanes }
                            if (availableLanes.isNotEmpty()) {
                                val lane = availableLanes.random()
                                coins.add(Coin(lane, 0f))
                            }
                        }
                        distance++
                    }
                }
            }

            LaunchedEffect(isGameOver) {
                if (!isGameOver) {
                    while (true) {
                        delay(moveInterval)
                        for (drop in raindrops) drop.y += dropSpeed
                        for (coin in coins) coin.y += dropSpeed

                        val hitDrop = raindrops.find { it.lane == fairyLane && it.y in (fairyY - 30)..(fairyY + 30) }
                        if (hitDrop != null) {
                            raindrops.remove(hitDrop)
                            lives--
                            MediaPlayer.create(context, R.raw.crash_sound).start()
                            ToastWithVibration(context)

                            if (lives <= 0) {
                                if (permissionGranted.value) {
                                    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                                        val lat = location?.latitude ?: 0.0
                                        val lon = location?.longitude ?: 0.0

                                        val geocoder = Geocoder(context, Locale.getDefault())
                                        val addresses = geocoder.getFromLocation(lat, lon, 1)
                                        val locationName = if (addresses != null && addresses.isNotEmpty()) {
                                            addresses[0].locality ?: addresses[0].featureName ?: "Unknown"
                                        } else {
                                            "Unknown"
                                        }

                                        val newScore = HighScore(
                                            score = distance,
                                            coins = collectedCoins,
                                            timestamp = System.currentTimeMillis(),
                                            locationName = locationName,
                                            latitude = lat,
                                            longitude = lon
                                        )
                                        HighScoreManager.saveHighScore(context, newScore)
                                    }
                                } else {
                                    val newScore = HighScore(
                                        score = distance,
                                        coins = collectedCoins,
                                        timestamp = System.currentTimeMillis(),
                                        locationName = "Unknown",
                                        latitude = 0.0,
                                        longitude = 0.0
                                    )
                                    HighScoreManager.saveHighScore(context, newScore)
                                }
                                isGameOver = true
                            }
                        }

                        val collectedCoin = coins.find { it.lane == fairyLane && it.y in (fairyY - 30)..(fairyY + 30) }
                        if (collectedCoin != null) {
                            coins.remove(collectedCoin)
                            collectedCoins++
                        }

                        raindrops.removeAll { it.y > screenHeightPx }
                        coins.removeAll { it.y > screenHeightPx }
                    }
                }
            }

            raindrops.forEach { drop ->
                Box(modifier = Modifier.offset(x = laneWidth * drop.lane, y = drop.y.dp)) {
                    Image(painter = painterResource(id = R.drawable.raindrop), contentDescription = "Raindrop", modifier = Modifier.size(Constants.DROP_SIZE.dp))
                }
            }
            coins.forEach { coin ->
                Box(modifier = Modifier.offset(x = laneWidth * coin.lane, y = coin.y.dp)) {
                    Image(painter = painterResource(id = R.drawable.coin), contentDescription = "Coin", modifier = Modifier.size(Constants.DROP_SIZE.dp))
                }
            }
            if (!isGameOver) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomStart) {
                    Image(
                        painter = painterResource(id = R.drawable.fairy_no_bg),
                        contentDescription = "Fairy",
                        modifier = Modifier
                            .padding(start = laneWidth * fairyLane, bottom = Constants.FAIRY_BOTTOM_PADDING.dp)
                            .size(Constants.FAIRY_HEIGHT.dp)
                    )
                }
            }
            if (isGameOver) {
                Box(modifier = Modifier.fillMaxSize().background(Color(0xAA000000)), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Game Over 😵", color = Color.White, modifier = Modifier.padding(16.dp))
                        Button(onClick = { onBackToMenu() }) { Text("Back to Menu") }
                    }
                }
            }
        }

        if (gameMode == GameMode.BUTTONS || gameMode == GameMode.BUTTONS_SLOW || gameMode == GameMode.BUTTONS_FAST) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Image(
                    painter = painterResource(id = R.drawable.arrow_right),
                    contentDescription = "Right Arrow",
                    modifier = Modifier.size(64.dp).clickable { if (fairyLane > 0) fairyLane-- }
                )
                Image(
                    painter = painterResource(id = R.drawable.arrow_left),
                    contentDescription = "Left Arrow",
                    modifier = Modifier.size(64.dp).clickable { if (fairyLane < Constants.NUM_LANES - 1) fairyLane++ }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GamePreview() {
    GameScreen(gameMode = GameMode.BUTTONS, onBackToMenu = {})
}
