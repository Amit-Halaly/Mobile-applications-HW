package com.example.mobile_applications_hw

import android.content.Context
import com.example.mobile_applications_hw.model.Score
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ScoreManager private constructor(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("Scores", Context.MODE_PRIVATE)
    private val gson = Gson()
    private var scoresRV: MutableList<Score> = loadScores()


    val scores: List<Score>
        get() {
            scoresRV = loadScores()
            return scoresRV
        }

    fun sortScores() {
        scoresRV.sortByDescending { it.score }
    }

    fun updateScore(newScore: Score) {
        scoresRV.add(newScore)
        sortScores()
        trimScores()
        saveScores()
    }

    private fun trimScores() {
        if (scoresRV.size > 10) {
            scoresRV = scoresRV.take(10).toMutableList()
        }

    }

    private fun saveScores() {
        val editor = sharedPreferences.edit()
        val json = gson.toJson(scoresRV)
        editor.putString("Scores", json)
        editor.apply()
    }

    fun loadScores(): MutableList<Score> {
        val json = sharedPreferences.getString("Scores", null)
        val type = object : TypeToken<MutableList<Score>>() {}.type
        return if (json != null) {
            gson.fromJson(json, type)
        } else {
            mutableListOf()
        }
    }


    companion object {
        private var instance: ScoreManager? = null
        fun getInstance(context: Context): ScoreManager {
            if (instance == null) {
                instance = ScoreManager(context)
            }
            return instance!!
        }
    }

    fun clearScores() {
        scoresRV.clear()
        saveScores()
    }

}