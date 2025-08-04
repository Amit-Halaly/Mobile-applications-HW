package com.example.mobile_applications_hw

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.mobile_applications_hw.fragments.HighScoreFragment
import com.example.mobile_applications_hw.fragments.MapFragment
import com.example.mobile_applications_hw.interfaces.Callback_HighScoreItemClicked
import com.example.mobile_applications_hw.interfaces.LocationUpdatedCallback
import com.example.mobile_applications_hw.model.Score
import com.example.mobile_applications_hw.utilities.LocationDetector

class RecordsActivity : AppCompatActivity(), LocationUpdatedCallback {

    private lateinit var main_FRAME_list: FrameLayout

    private lateinit var main_FRAME_map: FrameLayout

    private lateinit var mapFragment: MapFragment

    private lateinit var highScoreFragment: HighScoreFragment

    private lateinit var locationDetector: LocationDetector

    private var currentScore: Int = 0
    private var currentLat: Double = 0.0
    private var currentLon: Double = 0.0
    private var locationReceived = false
    private val LOCATION_PERMISSION_REQUEST_CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_records_map)

        findViews()
        initViews()
        locationDetector = LocationDetector(this, this)
        checkLocationPermission()

    }

    private fun findViews() {
        main_FRAME_list = findViewById(R.id.main_FRAME_list)
        main_FRAME_map = findViewById(R.id.main_FRAME_map)
    }

    private fun initViews() {
        mapFragment = MapFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.main_FRAME_map, mapFragment)
            .commit()

        highScoreFragment = HighScoreFragment()

        highScoreFragment.highScoreItemClicked = object : Callback_HighScoreItemClicked {
            override fun highScoreItemClicked(lat: Double, lon: Double) {
                mapFragment.placeMarkerAndZoom(lat, lon, "Score Location")
            }
        }

        supportFragmentManager.beginTransaction()
            .add(R.id.main_FRAME_list, highScoreFragment)
            .commit()
    }

    override fun onResume() {
        super.onResume()
        highScoreFragment.updateScore()
        loadScoreFromIntent()
    }

    private fun loadScoreFromIntent() {
        val bundle = intent.extras ?: return
        currentScore = bundle.getInt("Score", 0)

        if (locationReceived) {
            addScore()
        } else {
            Toast.makeText(this, "Waiting for location...", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addScore() {
        if (currentScore == 0) return
        if (currentLat == 0.0 || currentLon == 0.0) {
            Toast.makeText(this, "Location not available yet.", Toast.LENGTH_SHORT).show()
            return
        }
        val scores = ScoreManager.getInstance(this).scores
        if (scores.size < 10 || currentScore > scores.last().score) {
            val newScore = Score(currentScore, currentLat, currentLon)
            ScoreManager.getInstance(this).updateScore(newScore)
            highScoreFragment.updateScore()
        }
        currentScore = 0
    }

    override fun onLocationUpdated(latitude: Double, longitude: Double) {
        currentLat = latitude
        currentLon = longitude
        locationReceived = true
        if (currentScore != 0) {
            addScore()
        }
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            locationDetector.startLocationUpdates()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationDetector.startLocationUpdates()
            } else {
                Toast.makeText(
                    this,
                    "Location permission is required to save score locations.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        locationDetector.stopLocationUpdates()
    }
}