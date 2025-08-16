package com.example.mobile_applications_hw

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import com.example.mobile_applications_hw.interfaces.TiltCallback
import com.example.mobile_applications_hw.utilities.TiltDetector
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textview.MaterialTextView
import kotlin.jvm.java

class MainActivity : AppCompatActivity() {

    private lateinit var main_LBL_score: MaterialTextView

    private lateinit var hearts: Array<AppCompatImageView>

    private lateinit var leftArrow: ExtendedFloatingActionButton

    private lateinit var rightArrow: ExtendedFloatingActionButton

    private lateinit var car: Array<ImageView>

    private lateinit var obstacles: Array<Array<ImageView>>

    private lateinit var coins: Array<Array<ImageView>>

    private lateinit var gameManager: GameManager

    private lateinit var tiltDetector: TiltDetector

    private var isTiltMode = false

    private lateinit var scoreManager: ScoreManager





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        isTiltMode = intent.getBooleanExtra("IS_TILT_MODE", false)
        val isFastSpeed = intent.getBooleanExtra("GAME_SPEED", false)
        findViews()
        scoreManager = ScoreManager.getInstance(this)
        initGameManager()
        initViews()
        if (isTiltMode) {
            initTiltDetector()
            leftArrow.visibility = View.GONE
            rightArrow.visibility = View.GONE
        }
        gameManager.updateSpeed(isFastSpeed)
        gameManager.startGame()
    }

    private fun findViews() {
        main_LBL_score = findViewById(R.id.main_LBL_score)

        hearts = arrayOf(
            findViewById(R.id.main_IMG_heart0),
            findViewById(R.id.main_IMG_heart1),
            findViewById(R.id.main_IMG_heart2)
        )

        leftArrow = findViewById(R.id.main_FAB_LeftArrow)
        rightArrow = findViewById(R.id.main_FAB_RightArrow)
        car = arrayOf(
            findViewById(R.id.main_IMG_car7_0),
            findViewById(R.id.main_IMG_car7_1),
            findViewById(R.id.main_IMG_car7_2),
            findViewById(R.id.main_IMG_car7_3),
            findViewById(R.id.main_IMG_car7_4)
        )

        obstacles = arrayOf(
            arrayOf(
                findViewById(R.id.main_IMG_obstacle0_0),
                findViewById(R.id.main_IMG_obstacle0_1),
                findViewById(R.id.main_IMG_obstacle0_2),
                findViewById(R.id.main_IMG_obstacle0_3),
                findViewById(R.id.main_IMG_obstacle0_4)
            ), arrayOf(
                findViewById(R.id.main_IMG_obstacle1_0),
                findViewById(R.id.main_IMG_obstacle1_1),
                findViewById(R.id.main_IMG_obstacle1_2),
                findViewById(R.id.main_IMG_obstacle1_3),
                findViewById(R.id.main_IMG_obstacle1_4)
            ), arrayOf(
                findViewById(R.id.main_IMG_obstacle2_0),
                findViewById(R.id.main_IMG_obstacle2_1),
                findViewById(R.id.main_IMG_obstacle2_2),
                findViewById(R.id.main_IMG_obstacle2_3),
                findViewById(R.id.main_IMG_obstacle2_4)
            ), arrayOf(
                findViewById(R.id.main_IMG_obstacle3_0),
                findViewById(R.id.main_IMG_obstacle3_1),
                findViewById(R.id.main_IMG_obstacle3_2),
                findViewById(R.id.main_IMG_obstacle3_3),
                findViewById(R.id.main_IMG_obstacle3_4)
            ), arrayOf(
                findViewById(R.id.main_IMG_obstacle4_0),
                findViewById(R.id.main_IMG_obstacle4_1),
                findViewById(R.id.main_IMG_obstacle4_2),
                findViewById(R.id.main_IMG_obstacle4_3),
                findViewById(R.id.main_IMG_obstacle4_4)
            ), arrayOf(
                findViewById(R.id.main_IMG_obstacle5_0),
                findViewById(R.id.main_IMG_obstacle5_1),
                findViewById(R.id.main_IMG_obstacle5_2),
                findViewById(R.id.main_IMG_obstacle5_3),
                findViewById(R.id.main_IMG_obstacle5_4)
            ), arrayOf(
                findViewById(R.id.main_IMG_obstacle6_0),
                findViewById(R.id.main_IMG_obstacle6_1),
                findViewById(R.id.main_IMG_obstacle6_2),
                findViewById(R.id.main_IMG_obstacle6_3),
                findViewById(R.id.main_IMG_obstacle6_4)
            ), arrayOf(
                findViewById(R.id.main_IMG_obstacle7_0),
                findViewById(R.id.main_IMG_obstacle7_1),
                findViewById(R.id.main_IMG_obstacle7_2),
                findViewById(R.id.main_IMG_obstacle7_3),
                findViewById(R.id.main_IMG_obstacle7_4)
            )
        )

        coins = arrayOf(
            arrayOf(
                findViewById(R.id.main_IMG_coin0_0),
                findViewById(R.id.main_IMG_coin0_1),
                findViewById(R.id.main_IMG_coin0_2),
                findViewById(R.id.main_IMG_coin0_3),
                findViewById(R.id.main_IMG_coin0_4)
            ), arrayOf(
                findViewById(R.id.main_IMG_coin1_0),
                findViewById(R.id.main_IMG_coin1_1),
                findViewById(R.id.main_IMG_coin1_2),
                findViewById(R.id.main_IMG_coin1_3),
                findViewById(R.id.main_IMG_coin1_4)
            ), arrayOf(
                findViewById(R.id.main_IMG_coin2_0),
                findViewById(R.id.main_IMG_coin2_1),
                findViewById(R.id.main_IMG_coin2_2),
                findViewById(R.id.main_IMG_coin2_3),
                findViewById(R.id.main_IMG_coin2_4)
            ), arrayOf(
                findViewById(R.id.main_IMG_coin3_0),
                findViewById(R.id.main_IMG_coin3_1),
                findViewById(R.id.main_IMG_coin3_2),
                findViewById(R.id.main_IMG_coin3_3),
                findViewById(R.id.main_IMG_coin3_4)
            ), arrayOf(
                findViewById(R.id.main_IMG_coin4_0),
                findViewById(R.id.main_IMG_coin4_1),
                findViewById(R.id.main_IMG_coin4_2),
                findViewById(R.id.main_IMG_coin4_3),
                findViewById(R.id.main_IMG_coin4_4)
            ), arrayOf(
                findViewById(R.id.main_IMG_coin5_0),
                findViewById(R.id.main_IMG_coin5_1),
                findViewById(R.id.main_IMG_coin5_2),
                findViewById(R.id.main_IMG_coin5_3),
                findViewById(R.id.main_IMG_coin5_4)
            ), arrayOf(
                findViewById(R.id.main_IMG_coin6_0),
                findViewById(R.id.main_IMG_coin6_1),
                findViewById(R.id.main_IMG_coin6_2),
                findViewById(R.id.main_IMG_coin6_3),
                findViewById(R.id.main_IMG_coin6_4)
            ), arrayOf(
                findViewById(R.id.main_IMG_coin7_0),
                findViewById(R.id.main_IMG_coin7_1),
                findViewById(R.id.main_IMG_coin7_2),
                findViewById(R.id.main_IMG_coin7_3),
                findViewById(R.id.main_IMG_coin7_4)
            )
        )
    }

    private fun initGameManager() {
        gameManager = GameManager(
            context = this,
            car = car,
            obstacles = obstacles,
            coins = coins,
            hearts = hearts,
            onGameOver = { handleGameOver(gameManager.score) },
            onScoreUpdated = { newScore -> updateScoreUI(newScore) }
        )
    }

    private fun updateScoreUI(newScore: Int) {
        runOnUiThread {
            main_LBL_score.text = newScore.toString()
        }
    }

    private fun initViews() {
        main_LBL_score.text = gameManager.score.toString()

        if (!isTiltMode) {
            leftArrow.setOnClickListener {
                gameManager.moveCarLeft()
            }
            rightArrow.setOnClickListener {
                gameManager.moveCarRight()
            }
        }
    }

    private fun initTiltDetector() {
        tiltDetector = TiltDetector(
            context = this,
            tiltCallback = object : TiltCallback {
                override fun tiltX() {
                    if (tiltDetector.tiltCounterX > 0) {
                        gameManager.moveCarRight()
                    } else {
                        gameManager.moveCarLeft()
                    }
                }
            }
        )
        tiltDetector.start()
    }

    override fun onResume() {
        super.onResume()
        if (isTiltMode) {
            initTiltDetector()
            tiltDetector.start()
        }
    }

    override fun onStop() {
        super.onStop()
        if (isTiltMode) {
            tiltDetector.stop()
        }
    }

    override fun onPause() {
        super.onPause()
        if (isTiltMode) {
            tiltDetector.stop()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isTiltMode) {
            tiltDetector.stop()
        }
    }

    fun handleGameOver(finalScore: Int) {
        val intent = Intent(this, RecordsActivity::class.java)
        intent.putExtra("Score", finalScore)
        startActivity(intent)
        finish()
    }
}