package com.example.application1

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*

@SuppressLint("MissingPermission")
@Composable
fun MapViewSection(lat: Double, lon: Double) {
    val context = LocalContext.current

    AndroidView(
        factory = { ctx: Context ->
            MapView(ctx).apply {
                onCreate(null)
                onResume()
                getMapAsync { googleMap: GoogleMap ->
                    val location = LatLng(lat, lon)
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
                    googleMap.addMarker(MarkerOptions().position(location).title("High Score Location"))
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    )
}
