package com.example.new_gastracker

import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName


data class GasPrices(
    @SerializedName("Gasolina IO95") val gasolinaIO95: Double,
    @SerializedName("Gasóleo Rodoviário") val gasoleoRodoviario: Double,
    @SerializedName("Gasóleo Colorido e Marcado") val gasoleoColoridoMarcado: Double,
    @SerializedName("Gasolina IO98") val gasolinaIO98: Double
)

data class GasInfo(
    @SerializedName("Start date") val startDate: String,
    @SerializedName("End date") val endDate: String,
    @SerializedName("Gas") val gas: GasPrices
)

data class GasResponse(
    @SerializedName("previous") val previous: GasInfo,
    @SerializedName("current") val current: GasInfo
)

data class PlacesResponse(
    val results: List<Place>
)

data class Place(
    val name: String,
    val geometry: Geometry
)

data class Geometry(
    val location: Location
)

data class Location(
    val lat: Double,
    val lng: Double
)


data class GasStation(
    val name: String,
    val location: LatLng
)
