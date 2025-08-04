package com.example.mobile_applications_hw

import android.app.Application
import com.example.mobile_applications_hw.utilities.SignalManager

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        SignalManager.init(this)
        ScoreManager.getInstance(this)
        ScoreManager.getInstance(this).loadScores()

    }
}