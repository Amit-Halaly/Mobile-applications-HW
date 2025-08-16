package com.example.mobile_applications_hw

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import com.example.mobile_applications_hw.utilities.SignalManager
import com.example.mobile_applications_hw.utilities.SingleSoundPlayer
import kotlin.random.Random

class GameManager(
    context: Context,
    private val car: Array<ImageView>,
    private val obstacles: Array<Array<ImageView>>,
    private val coins: Array<Array<ImageView>>,
    private val hearts: Array<AppCompatImageView>,
    private val onGameOver: () -> Unit,
    private val onScoreUpdated: (Int) -> Unit
) {

    private val singleSoundPlayer = SingleSoundPlayer(context)

    var score: Int = 0
        private set

    private var carPosition = 2

    private var lives = 3

    private var gameOver = false

    val handler = Handler(Looper.getMainLooper())

    private var gameSpeed: Long = 1000L


    fun updateSpeed(isFast: Boolean) {
        gameSpeed = if (isFast) 500L else 1000L
    }

    fun resetGame() {
        lives = 3
        gameOver = false

        resetHearts()
        resetScore()
        resetCar()
        resetObstacles()
        resetCoins()
        placeObstaclesRandomly()
        placeCoinsRandomly()
    }


    private fun resetCar() {
        for (i in car.indices) {
            car[i].visibility = View.INVISIBLE
        }
        car[carPosition].visibility = View.VISIBLE
    }


    private fun resetHearts() {
        for (heart in hearts) {
            heart.visibility = View.VISIBLE
        }
    }


    private fun resetObstacles() {
        for (row in obstacles) {
            for (obstacles in row) {
                obstacles.visibility = View.INVISIBLE
            }
        }
    }

    private fun resetCoins() {
        for (row in coins) {
            for (coin in row) {
                coin.visibility = View.INVISIBLE
            }
        }
    }

    private fun resetScore() {
        score = 0
        onScoreUpdated(score)
    }


    private fun placeObstaclesRandomly() {
        val firstRow = obstacles[0]
        for (i in firstRow.indices) {
            firstRow[i].visibility = View.INVISIBLE
        }
        val randomIndex = Random.nextInt(firstRow.size)
        firstRow[randomIndex].visibility = View.VISIBLE
    }

    private fun placeCoinsRandomly() {
        val firstRowCoins = coins[0]
        val firstRowObstacles = obstacles[0]
        for (i in firstRowCoins.indices) {
            firstRowCoins[i].visibility = View.INVISIBLE
        }
        var randomIndex: Int
        do {
            randomIndex = Random.nextInt(firstRowCoins.size)
        } while (firstRowObstacles[randomIndex].visibility == View.VISIBLE)
        firstRowCoins[randomIndex].visibility = View.VISIBLE
    }


    fun moveCarLeft() {
        if (!gameOver && carPosition > 0) {
            car[carPosition].visibility = View.INVISIBLE
            carPosition--
            car[carPosition].visibility = View.VISIBLE
        }
    }


    fun moveCarRight() {
        if (!gameOver && carPosition < 4) {
            car[carPosition].visibility = View.INVISIBLE
            carPosition++
            car[carPosition].visibility = View.VISIBLE
        }
    }


    fun startGame() {
        resetGame()
        val gameLoopRunnable = object : Runnable {
            override fun run() {
                if (!gameOver) {
                    moveObstaclesDown()
                    moveCoinsDown()
                    placeObstaclesRandomly()
                    placeCoinsRandomly()
                    checkCollisions()
                    handler.postDelayed(this, gameSpeed)
                }
            }
        }
        handler.post(gameLoopRunnable)
    }

    private fun moveObstaclesDown() {
        for (row in obstacles.indices.reversed()) {
            for (col in obstacles[row].indices) {
                val cop = obstacles[row][col]
                if (cop.visibility == View.VISIBLE) {
                    cop.visibility = View.INVISIBLE
                    if (row + 1 < obstacles.size) {
                        obstacles[row + 1][col].visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun moveCoinsDown() {
        for (row in coins.indices.reversed()) {
            for (col in coins[row].indices) {
                val coin = coins[row][col]
                if (coin.visibility == View.VISIBLE) {
                    coin.visibility = View.INVISIBLE
                    if (row + 1 < coins.size) {
                        coins[row + 1][col].visibility = View.VISIBLE
                    }
                }
            }
        }
    }


    private fun checkCollisions() {
        if (obstacles.last()[carPosition].visibility == View.VISIBLE) {
            handleCollision()
        }
        if (coins.last()[carPosition].visibility == View.VISIBLE) {
            coinCollected()
        }
    }


    private fun handleCollision() {
       // singleSoundPlayer.playSound(R.raw.boom_cinema)
        lives--
        updateScore(-50)
        toastAndVibrate()
        hearts[lives].visibility = View.INVISIBLE
        if (lives == 0) {
            gameOver = true
            onGameOver()
        }
    }


    private fun toastAndVibrate() {
        if (lives == 0) {
            SignalManager.getInstance().toast("Game Over your score is : " + score)
            SignalManager.getInstance().vibrate()
        } else {
            SignalManager.getInstance().toast("Collision! -50")
            SignalManager.getInstance().vibrate()
        }
    }

    private fun coinCollected() {
       // singleSoundPlayer.playSound(R.raw.coin_collected)
        updateScore(100)
        SignalManager.getInstance().toast("Coin Collected! +100")
        SignalManager.getInstance().vibrate()

    }

    private fun updateScore(points: Int) {
        score += points
        if (score < 0) {
            score = 0
        }
        onScoreUpdated(score)
    }
}