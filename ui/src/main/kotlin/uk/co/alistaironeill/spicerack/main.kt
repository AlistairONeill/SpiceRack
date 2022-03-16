package uk.co.alistaironeill.spicerack

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.AlertDialog
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import uk.co.alistaironeill.spicerack.error.AlertSingleton
import uk.co.alistaironeill.spicerack.home.HomeScreen
import uk.co.alistaironeill.spicerack.leds.LedScreen
import uk.co.alistaironeill.spicerack.navigation.NavBar
import uk.co.alistaironeill.spicerack.navigation.Screen
import uk.co.alistaironeill.spicerack.spice.InMemorySpiceSource
import uk.co.alistaironeill.spicerack.spices.SpicesScreen


fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Spice Rack Configuration",
        state = rememberWindowState(width = 300.dp, height = 300.dp)
    ) {
        val currentScreen = remember { mutableStateOf(Screen.Home) }
        val spiceSource = InMemorySpiceSource()
        AlertSingleton.error = remember { mutableStateOf(null) }
        MaterialTheme {
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom
            ) {
                Box(Modifier.weight(1f)) {
                    when (currentScreen.value) {
                        Screen.Home -> HomeScreen()
                        Screen.Spices -> SpicesScreen(spiceSource)
                        Screen.LEDs -> LedScreen()
                    }
                }

                NavBar(currentScreen)

                val error = AlertSingleton.error.value
                if (error != null) {
                    @OptIn(ExperimentalMaterialApi::class)
                    AlertDialog(
                        onDismissRequest = { AlertSingleton.error.value = null },
                        buttons = { },
                        title = { Text("Uh Oh! Something has gone wrong!") },
                        text = { Text(error.msg) }
                    )
                }
            }
        }
    }
}