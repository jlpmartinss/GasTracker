package com.example.new_gastracker.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.app.ComponentActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.example.new_gastracker.GasStation
import com.example.new_gastracker.PlacesResponse
import com.example.new_gastracker.REQUEST_LOCATION_PERMISSION
import com.example.new_gastracker.RetrofitClient
import com.example.new_gastracker.ui.theme.Black
import com.example.new_gastracker.ui.theme.Orange
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
fun LocationMapScreen(navController: NavHostController) {
    val context = LocalContext.current
    var currentLocation by remember { mutableStateOf<LatLng?>(null) }
    var permissionGranted by remember { mutableStateOf(false) }
    var gasStations by remember { mutableStateOf<List<GasStation>>(emptyList()) }
    val zoomLevel by remember { mutableFloatStateOf(15f) }


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
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text("Location permission not granted")
        }
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



    Box(modifier = Modifier.fillMaxSize()) {

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
        BackButton(navController = navController)


    }

}

@Composable
fun BackButton(navController: NavHostController) {
    Box(modifier = Modifier
        .statusBarsPadding()
        .padding(start = 8.dp, top = 8.dp)
        .background(Black, RoundedCornerShape(20.dp))
    ) {

        IconButton(
            onClick = {
                navController.navigate(Screens.GasInfoScreen.screen)
            },
            modifier = Modifier
                .align(Alignment.TopStart)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Orange,
                modifier = Modifier.size(32.dp)
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


