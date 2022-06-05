package com.vsaytech.mvvmweather.extensions

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.provider.Settings
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.vsaytech.mvvmweather.ui.currentweather.LastLocationListener

val locationPermissions = arrayOf(
    Manifest.permission.ACCESS_NETWORK_STATE,
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION
)

private fun Fragment.checkAppPermission(): Boolean {
    locationPermissions.forEach { permission ->
        if (ContextCompat.checkSelfPermission(requireContext(), permission) ==
            PackageManager.PERMISSION_DENIED
        ) {
            return false
        }
    }
    return true
}

//Request Location
fun Fragment.getLastLocation(
    fusedLocationClient: FusedLocationProviderClient,
    lastLocationListener: LastLocationListener
) {
    if (checkAppPermission()) {
        activity?.let {
            if (isLocationEnabled()) {
                fusedLocationClient.lastLocation.addOnCompleteListener(it) { task ->
                    val location: Location? = task.result
                    if (location != null) {
                        lastLocationListener.onLastLocationFound(location)
                        //viewModel.refreshCurrentWeatherFromRepository("${location.latitude},${location.longitude}")
                    }
                }
            } else {
                Toast.makeText(it, "Please turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        }
    } else {
        lastLocationListener.onCheckPermissonFailed()
    }
}

private fun Fragment.isLocationEnabled(): Boolean {
    val locationManager: LocationManager =
        activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
        LocationManager.NETWORK_PROVIDER
    )
}