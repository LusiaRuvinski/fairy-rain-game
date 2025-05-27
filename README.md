# 🌧️ Fairy rain – Android Game
**Fairy rain** is a magical arcade-style Android game where you control a tiny fairy who must dodge falling raindrops, collect golden coins, and survive as long as possible. 

## 🎮 Features
- 🧚 Control a fairy using **buttons** or **device tilt (sensor)**
- ☔ **Raindrops** fall from the top – avoid them to stay alive!
- 🪙 **Collect coins** to boost your score
- ❤️ **Lives system** – you start with limited hearts
- 📏 **Distance tracker** – see how far you’ve gone
- 🕹️ Choose between **Slow Mode** or **Fast Mode**
- 💥 **Crash sound effect** when hit by a raindrop
- 🔚 **Game Over screen** with option to restart
- 📊 **High Score table** saved locally (top 10 scores)
- 🗺️ **Google Maps** view to display where each score was achieved
- 🎯 Tapping a high score shows its location on a map in split-screen mode

## 🛠️ Tech Stack
- **Kotlin** + **Jetpack Compose**
- **SensorManager** (for tilt detection)
- **MediaPlayer** (for sound effects)
- **Google Maps SDK**
- **Fused Location Provider** (GPS)
- **SharedPreferences** (to store scores)

- ## 📂 Project Structure

| MainActivity.kt | App launcher and screen controller |
| GameScreen.kt | Main gameplay screen |
| StartScreen.kt | Opening screen with mode selection |
| SpeedOptionsScreen.kt | Speed mode selector (Slow / Fast) |
|  HighScore.kt	|Data class representing a high score entry including score, coins, location and timestamp|
| HighScoresScreen.kt | Scoreboard only |
| HighScoresWithMapScreen.kt | Scoreboard + Map (split view) |
| HighScoreManager.kt | Handles saving & loading scores |
| MapViewSection.kt | Google Maps integration |
|  MapScreen.kt	| Used if you want to show only the map with location (optional)
| Constants.kt | Game constants and configuration |
|  GameUtils.kt	| Contains helper functions like vibration or toast display|
|  Raindrop.kt	| Data class representing a raindrop (position, speed)|
|  coin.kt	| Data class representing a coin (lane, position)|
|  GameMode.kt| Enum defining available game modes: BUTTONS_SLOW, BUTTONS_FAST, SENSOR|

## 📸 Screenshots
<img src="https://github.com/LusiaRuvinski/fairy-rain-game/blob/master/WhatsApp%20Image%202025-05-27%20at%2016.12.11.jpeg?raw=true" width="300"/>
<img src="https://github.com/LusiaRuvinski/fairy-rain-game/blob/master/35a68985-5e00-4201-a573-1fb1982d2e8e.jpeg?raw=true" width="300"/>  
<img src="https://github.com/LusiaRuvinski/fairy-rain-game/blob/master/3b2b4e1b-6036-43aa-a482-0d7c9cdd5d26.jpeg?raw=true" width="300"/>  
<img src="https://github.com/LusiaRuvinski/fairy-rain-game/blob/master/52d07547-4638-4073-a9d1-2e4b8ada8619.jpeg?raw=true" width="300"/>  
<img src="https://github.com/LusiaRuvinski/fairy-rain-game/blob/master/5bc26af9-1c05-438a-b186-5f3559aa3a10.jpeg?raw=true" width="300"/>  
<img src="https://github.com/LusiaRuvinski/fairy-rain-game/blob/master/638a3354-9d2d-4ae9-aaf0-d45e557e1094.jpeg?raw=true" width="300"/>  
<img src="https://github.com/LusiaRuvinski/fairy-rain-game/blob/master/bacfa205-ae72-41bc-95d3-4ad7e25c5e15.jpeg?raw=true" width="300"/>
















