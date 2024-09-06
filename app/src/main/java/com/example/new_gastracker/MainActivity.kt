package com.example.new_gastracker

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.new_gastracker.screens.GasInfoScreen
import com.example.new_gastracker.screens.LocationMapScreen
import com.example.new_gastracker.screens.Screens
import com.example.new_gastracker.ui.theme.Black
import com.example.new_gastracker.ui.theme.Grey
import com.example.new_gastracker.ui.theme.New_GasTrackerTheme
import com.example.new_gastracker.ui.theme.Orange

class MainActivity : ComponentActivity() {
    private val gasViewModel: GasViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        installSplashScreen().apply {
            !gasViewModel.isReady.value
        }
        setContent {
            New_GasTrackerTheme {
                MyBottomAppBar(gasViewModel = gasViewModel, showBottomBar = true)
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MyBottomAppBar(gasViewModel: GasViewModel, showBottomBar: Boolean) {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Black),
        bottomBar = {
            // Check current route to decide whether to show the bottom bar
            val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
            if (currentRoute != Screens.LocationMapScreen.screen) {
                CustomBottomAppBar(navController = navController)
            }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = Screens.GasInfoScreen.screen,
            modifier = Modifier
                .background(Black)
                .fillMaxSize()
        ) {
            composable(Screens.GasInfoScreen.screen) {
                GasInfoScreen()
            }
            composable(Screens.LocationMapScreen.screen) {
                LocationMapScreen(navController)
            }
        }
    }
}

@Composable
fun CustomBottomAppBar(navController: NavHostController) {
    val selectedIcon = remember { mutableStateOf(Icons.Default.Home) }

    val bottomAppBarWidth = 250.dp
    val bottomAppBarHeight = 56.dp
    val iconSize = 30.dp

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .width(bottomAppBarWidth)
                .height(bottomAppBarHeight)
                .clip(RoundedCornerShape(48.dp))
                .background(Grey)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButtonWithIcon(
                    icon = Icons.Default.Home,
                    selectedIcon = selectedIcon.value,
                    onClick = {
                        selectedIcon.value = Icons.Default.Home
                        navController.navigate(Screens.GasInfoScreen.screen) {
                            popUpTo(0)
                        }
                    },
                    iconSize = iconSize,
                    selectedTint = Orange
                )

                IconButtonWithIcon(
                    icon = Icons.Default.LocationOn,
                    selectedIcon = selectedIcon.value,
                    onClick = {
                        selectedIcon.value = Icons.Default.LocationOn
                        navController.navigate(Screens.LocationMapScreen.screen) {
                            popUpTo(0)
                        }
                    },
                    iconSize = iconSize,
                    selectedTint = Orange
                )
            }
        }
    }
}

@Composable
fun IconButtonWithIcon(
    icon: ImageVector,
    selectedIcon: ImageVector,
    onClick: () -> Unit,
    iconSize: Dp,
    selectedTint: Color
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.size(iconSize + 16.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (selectedIcon == icon) selectedTint else Color.White,
            modifier = Modifier.size(iconSize)
        )
    }
}
