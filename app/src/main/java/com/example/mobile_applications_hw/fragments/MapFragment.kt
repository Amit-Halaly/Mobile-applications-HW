package com.example.mobile_applications_hw.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mobile_applications_hw.R
import com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map_fragment)
                as? SupportMapFragment
        mapFragment?.getMapAsync(this)
       // SupportMapFragment.newInstance().getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        map.uiSettings.isMapToolbarEnabled = true
        map.uiSettings.isZoomControlsEnabled = true
        val defaultLocation = LatLng(32.084932, 34.835226)
        googleMap.moveCamera(newLatLngZoom(defaultLocation, 10f))
    }

    fun placeMarkerAndZoom(lat: Double, lon: Double, title: String) {
        val position = LatLng(lat, lon)

        googleMap.addMarker(
            MarkerOptions()
                .position(position)
                .title(title)
        )
        googleMap.animateCamera(newLatLngZoom(position, 14f))
    }

}