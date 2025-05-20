package com.example.application1

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object HighScoreManager {
    private const val PREF_NAME = "high_scores"
    private const val KEY = "scores"
    private val gson = Gson()

    fun saveHighScore(context: Context, newScore: HighScore) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val existingJson = prefs.getString(KEY, null)

        val type = object : TypeToken<MutableList<HighScore>>() {}.type
        val scores = if (existingJson != null)
            gson.fromJson<MutableList<HighScore>>(existingJson, type)
        else
            mutableListOf()

        scores.add(newScore)


        val top10 = scores.sortedByDescending { it.score }.take(10)

        prefs.edit().putString(KEY, gson.toJson(top10)).apply()
    }

    fun loadHighScores(context: Context): List<HighScore> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val existingJson = prefs.getString(KEY, null)

        val type = object : TypeToken<List<HighScore>>() {}.type
        return if (existingJson != null)
            gson.fromJson(existingJson, type)
        else
            emptyList()
    }
}
