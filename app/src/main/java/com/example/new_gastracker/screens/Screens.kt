package com.example.new_gastracker.screens

sealed class Screens (val screen: String) {
    data object GasInfoScreen : Screens("GasInfoScreen")
    data object LocationMapScreen : Screens("LocationMapScreen")
}