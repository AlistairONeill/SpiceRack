package uk.co.alistaironeill.spicerack.navigation

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState

@Composable
fun NavBar(
    currentScreen: MutableState<Screen>
) {
    BottomNavigation(
        backgroundColor = MaterialTheme.colors.surface
    ) {
        Screen.values()
            .forEach { screen ->
                BottomNavigationItem(
                    icon = { Icon(screen.icon, contentDescription = screen.name) },
                    selected = currentScreen.value == screen,
                    onClick = { currentScreen.value = screen },
                    label = { Text(text = screen.name) }
                )
            }
    }
}
