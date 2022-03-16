package uk.co.alistaironeill.spicerack.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun NavBar(currentScreen: MutableState<Screen>) {
    Row(Modifier.fillMaxWidth()) {
        Screen.values()
            .forEach { screen ->
                NavBarItem(screen, currentScreen)
            }
    }
}

@Composable
fun NavBarItem(
    screen: Screen,
    currentScreen: MutableState<Screen>
) {
    val isSelected = currentScreen.value == screen
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(20.dp)
            .width(100.dp)
            .clickable { currentScreen.value = screen }
    ) {
        Text(
            screen.name,
            Modifier
                .padding(8.dp)
        )

        Divider(
            modifier = Modifier
                .height(2.dp)
                .background(if (isSelected) Color.Black else Color.Transparent)
        )
    }
}