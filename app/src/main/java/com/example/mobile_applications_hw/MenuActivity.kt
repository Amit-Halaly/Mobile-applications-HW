package com.example.mobile_applications_hw

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import com.google.android.material.button.MaterialButton
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.textview.MaterialTextView

class MenuActivity : AppCompatActivity() {

    private lateinit var title_IMG_car: AppCompatImageView

    private lateinit var title_IMG_obstacle: AppCompatImageView

    private lateinit var title_LBL_title: MaterialTextView

    private lateinit var title_BTN_start: MaterialButton

    private lateinit var title_BTN_records: MaterialButton

    private lateinit var title_SWITCH_Mode: MaterialSwitch

    private lateinit var title_SWITCH_speed: MaterialSwitch


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        findViews()
        initViews()

    }

    private fun findViews() {
        title_IMG_car = findViewById(R.id.title_IMG_car)
        title_IMG_obstacle = findViewById(R.id.title_IMG_obstacle)
        title_LBL_title = findViewById(R.id.title_LBL_title)
        title_BTN_start = findViewById(R.id.title_BTN_start)
        title_BTN_records = findViewById(R.id.title_BTN_records)
        title_SWITCH_Mode = findViewById(R.id.title_SWITCH_Mode)
        title_SWITCH_speed = findViewById(R.id.title_SWITCH_speed)
    }

    private fun initViews() {
        title_BTN_start.setOnClickListener {
            onStartClicked()
        }

        title_SWITCH_Mode.setOnCheckedChangeListener { _, isChecked ->
            val isTiltMode = title_SWITCH_Mode.isChecked
            Intent().apply {
                putExtra("IS_TILT_MODE", isTiltMode)

            }
        }

        title_SWITCH_speed.setOnCheckedChangeListener { _, isChecked ->
            val speed = title_SWITCH_speed.isChecked
            Intent().apply {
                putExtra("GAME_SPEED", speed)

            }
        }

        title_BTN_records.setOnClickListener {
            val intent = Intent(this, RecordsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun onStartClicked() {
        val isTiltMode = title_SWITCH_Mode.isChecked
        val isFastSpeed = title_SWITCH_speed.isChecked

        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("GAME_MODE", if (isTiltMode) "SENSORS" else "BUTTONS")
            putExtra("IS_TILT_MODE", isTiltMode)
            putExtra("GAME_SPEED", isFastSpeed)
        }
        startActivity(intent)
    }


}