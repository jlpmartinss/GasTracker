package com.example.new_gastracker

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GasViewModel : ViewModel() {

    private val _gasResponse = MutableStateFlow<GasResponse?>(null)
    val gasResponse: StateFlow<GasResponse?> = _gasResponse
    private val _isReady = MutableStateFlow(false)
    val isReady = _isReady.asStateFlow()

    init {
        fetchGasInfo()
    }

    private fun fetchGasInfo() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getGasInfo()
                Log.d("GasViewModel", "Response: $response") // Add this line
                _gasResponse.value = response
                _isReady.value = true
            } catch (e: Exception) {
                Log.e("GasViewModel", "Error fetching data", e) // Add this line
            }
        }
    }

}
