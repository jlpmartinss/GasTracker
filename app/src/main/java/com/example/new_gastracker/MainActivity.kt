package com.example.new_gastracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.new_gastracker.ui.theme.New_GasTrackerTheme

class MainActivity : ComponentActivity() {
    private val gasViewModel: GasViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            New_GasTrackerTheme {
                Surface(modifier = Modifier.fillMaxSize()){
                    //GasInfoScreen(gasViewModel = gasViewModel)
                    //GasStationMapScreen()
                    LocationMapScreen()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    New_GasTrackerTheme {
        Greeting("Android")
    }
}