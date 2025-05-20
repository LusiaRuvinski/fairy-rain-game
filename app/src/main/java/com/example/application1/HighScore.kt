package com.example.application1

data class HighScore(
    val score: Int,
    val coins: Int,
    val timestamp: Long,
    val locationName: String,
    val latitude: Double,
    val longitude: Double
)

