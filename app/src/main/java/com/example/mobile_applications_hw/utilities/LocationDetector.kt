package com.example.mobile_applications_hw.utilities

import android.content.Context
import android.location.Location
import android.os.Looper
import com.example.mobile_applications_hw.interfaces.LocationUpdatedCallback
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import java.util.concurrent.TimeUnit
import kotlin.jvm.Throws

class LocationDetector (context: Context, private val callback: LocationUpdatedCallback) {
    private var fusedLocationProviderClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    private val locationRequest: LocationRequest = LocationRequest
        .Builder(TimeUnit.SECONDS.toMillis(60))
        .setMinUpdateIntervalMillis(TimeUnit.SECONDS.toMillis(30))
        .setPriority(LocationRequest.PRIORITY_LOW_POWER)
        .build()


    var currentLocation: Location? = null
    private lateinit var locationListener: LocationListener

    init {
        initLocationListener()
    }

    private fun initLocationListener(){
        locationListener = LocationListener {
                loc -> currentLocation = loc
            callback.onLocationUpdated(loc.latitude, loc.longitude)
        }
    }

    @Throws(SecurityException::class)
    fun startLocationUpdates() {
        fusedLocationProviderClient
            .requestLocationUpdates(
                locationRequest,
                locationListener,
                Looper.getMainLooper()
            )
    }

    fun stopLocationUpdates() {
        fusedLocationProviderClient
            .removeLocationUpdates(locationListener)
    }


}