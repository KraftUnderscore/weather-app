package com.pieta.weatherapp.data

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices

object LocationHelper {
    private fun permissionsCheck(context: Context) : Boolean {
        val hasFineLocationPermission =
                ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

        val hasCoarseLocationPermission =
                ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

        return hasCoarseLocationPermission && hasFineLocationPermission
    }

    fun requestPermissions(activity: Activity) {
        if (!permissionsCheck(context = activity.applicationContext)) {
            val permissionsToRequest = mutableListOf<String>()
            permissionsToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION)
            permissionsToRequest.add(Manifest.permission.ACCESS_COARSE_LOCATION)
            ActivityCompat.requestPermissions(activity, permissionsToRequest.toTypedArray(), 0)
        }
    }

    @SuppressLint("MissingPermission")
    fun getLocation(context: Context, f: (Float, Float) -> Unit) {
        if(!permissionsCheck(context)) return
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                f(location.latitude.toFloat(), location.longitude.toFloat())
            } else {
                f(0f, 0f)
            }
        }
    }
}