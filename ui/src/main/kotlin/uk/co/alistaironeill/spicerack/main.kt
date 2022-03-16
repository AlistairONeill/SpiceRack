package uk.co.alistaironeill.spicerack

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import uk.co.alistaironeill.spicerack.home.HomeScreen
import uk.co.alistaironeill.spicerack.leds.LedScreen
import uk.co.alistaironeill.spicerack.navigation.NavBar
import uk.co.alistaironeill.spicerack.navigation.Screen
import uk.co.alistaironeill.spicerack.spices.SpicesScreen

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Spice Rack Configuration",
        state = rememberWindowState(width = 300.dp, height = 300.dp)
    ) {
        val currentScreen = remember { mutableStateOf(Screen.Home) }

        MaterialTheme {
            Column(Modifier.fillMaxSize(), Arrangement.spacedBy(10.dp)) {
                NavBar(currentScreen)

                when (currentScreen.value) {
                    Screen.Home -> HomeScreen()
                    Screen.Spices -> SpicesScreen()
                    Screen.LEDs -> LedScreen()
                }
            }
        }
    }
}