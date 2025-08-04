package com.example.mobile_applications_hw

import android.content.Context
import android.os.Handler
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class GameManager(
    private val context: Context,
    private val car: Array<ImageView>,
    private val obstacles: Array<Array<ImageView>>,
    private val coins: Array<Array<ImageView>>,
    private val hearts: Array<AppCompatImageView>,
    private val leftButton: ExtendedFloatingActionButton,
    private val rightButton: ExtendedFloatingActionButton,
    private val onGameOver: (Int) -> Unit,
    private val onScoreUpdated: (Int) -> Unit
) {
    private val handler = Handler()
    private val TICK_SLOW = 800L
    private val TICK_FAST = 400L
    private var tickInterval = TICK_SLOW

    private var currentLane = 2
    var score = 0
        private set
    private var lives = 3

    fun updateSpeed(isFast: Boolean) {
        tickInterval = if (isFast) TICK_FAST else TICK_SLOW
    }

    fun startGame() {
        resetAll()
        updateCarUI()
        handler.postDelayed(gameLoopRunnable, tickInterval)
        leftButton.setOnClickListener { moveCarLeft() }
        rightButton.setOnClickListener { moveCarRight() }
    }

    private val gameLoopRunnable = object : Runnable {
        override fun run() {
            stepObstaclesAndCoins()
            checkCollisions()
            handler.postDelayed(this, tickInterval)
        }
    }

    private fun resetAll() {
        score = 0
        lives = hearts.size
        onScoreUpdated(score)
        hearts.forEach { it.visibility = View.VISIBLE }
        obstacles.flatten().forEach { it.visibility = View.INVISIBLE }
        coins.flatten().forEach    { it.visibility = View.INVISIBLE }
        spawnInitialRow()
    }

    private fun spawnInitialRow() {
        val row0Obs = obstacles[0]
        val row0Coins = coins[0]
        for (col in 0 until 5) {
            if ((0..1).random() == 0) {
                row0Obs[col].visibility = View.VISIBLE
                row0Coins[col].visibility = View.INVISIBLE
            } else {
                row0Coins[col].visibility = View.VISIBLE
                row0Obs[col].visibility = View.INVISIBLE
            }
        }
    }

    private fun stepObstaclesAndCoins() {
        for (col in 0 until 5) {
            obstacles.last()[col].visibility = View.INVISIBLE
            coins.last()[col].visibility     = View.INVISIBLE
        }
        for (row in obstacles.size - 1 downTo 1) {
            for (col in 0 until 5) {
                obstacles[row][col].visibility = obstacles[row - 1][col].visibility
                coins[row][col].visibility     = coins[row - 1][col].visibility
            }
        }
        spawnInitialRow()
        updateCarUI()
    }

    private fun checkCollisions() {
        val obs = obstacles.last()[currentLane]
        val coin = coins.last()[currentLane]
        if (obs.visibility == View.VISIBLE) {
            lives--
            hearts[lives].visibility = View.INVISIBLE
            obs.visibility = View.INVISIBLE
            if (lives == 0) {
                handler.removeCallbacks(gameLoopRunnable)
                onGameOver(score)
            }
        } else if (coin.visibility == View.VISIBLE) {
            score += 10
            onScoreUpdated(score)
            coin.visibility = View.INVISIBLE
        }
    }

    fun moveCarLeft() {
        if (currentLane > 0) {
            currentLane--
            updateCarUI()
        }
    }

    fun moveCarRight() {
        if (currentLane < car.size - 1) {
            currentLane++
            updateCarUI()
        }
    }

    private fun updateCarUI() {
        car.forEachIndexed { index, iv ->
            iv.visibility = if (index == currentLane) View.VISIBLE else View.INVISIBLE
        }
    }

    fun stopGame() {
        handler.removeCallbacks(gameLoopRunnable)
    }
}
