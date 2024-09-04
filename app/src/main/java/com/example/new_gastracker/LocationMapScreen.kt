package com.example.new_gastracker

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.app.ComponentActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@SuppressLint("RestrictedApi")
@Composable
fun LocationMapScreen() {
    val context = LocalContext.current
    var currentLocation by remember { mutableStateOf<LatLng?>(null) }
    var permissionGranted by remember { mutableStateOf(false) }
    var gasStations by remember { mutableStateOf<List<GasStation>>(emptyList()) }
    var zoomLevel by remember { mutableStateOf(15f) }


    val permissionState = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    LaunchedEffect(Unit) {
        if (permissionState == PackageManager.PERMISSION_GRANTED) {
            permissionGranted = true
        } else {
            ActivityCompat.requestPermissions(
                context as ComponentActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }
    }

    if (!permissionGranted) {
        Text("Location permission not granted")
        return
    }

    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    LaunchedEffect(Unit) {
        val locationResult: Task<android.location.Location> = fusedLocationClient.lastLocation
        locationResult.addOnCompleteListener { task ->
            if (task.isSuccessful && task.result != null) {
                val location = task.result
                currentLocation = LatLng(location.latitude, location.longitude)

                fetchNearbyGasStations(location.latitude, location.longitude) { stations ->
                    gasStations = stations
                }
            }
        }
    }

    val cameraPositionState = rememberCameraPositionState {
        currentLocation?.let {
            position = CameraPosition.fromLatLngZoom(it, zoomLevel)
        }
    }

    LaunchedEffect(currentLocation, zoomLevel) {
        currentLocation?.let {
            cameraPositionState.position = CameraPosition.fromLatLngZoom(it, zoomLevel)
        }
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        currentLocation?.let {
            Marker(
                state = MarkerState(position = it),
                title = "You are here!",
                snippet = "Current location"
            )
        }

        gasStations.forEach { station ->
            Marker(
                state = MarkerState(position = station.location),
                title = station.name,
                snippet = "Nearby gas station",
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)
            )
        }
    }
}


private fun fetchNearbyGasStations(lat: Double, lng: Double, onResult: (List<GasStation>) -> Unit) {
    val apiService = RetrofitClient.apiService
    val call = apiService.getNearbyGasStations("$lat,$lng", 5000) // 5km radius

    call.enqueue(object : Callback<PlacesResponse> {
        override fun onResponse(call: Call<PlacesResponse>, response: Response<PlacesResponse>) {
            if (response.isSuccessful) {
                response.body()?.results?.let { places ->
                    // Convert places to GasStation objects
                    val stations = places.map {
                        GasStation(
                            name = it.name,
                            location = LatLng(it.geometry.location.lat, it.geometry.location.lng)
                        )
                    }
                    onResult(stations)
                }
            }
        }

        override fun onFailure(call: Call<PlacesResponse>, t: Throwable) {
            // Handle failure
        }
    })
}


const val REQUEST_LOCATION_PERMISSION = 1
