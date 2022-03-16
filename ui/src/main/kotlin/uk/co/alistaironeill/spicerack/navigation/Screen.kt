package uk.co.alistaironeill.spicerack.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

enum class Screen(val icon: ImageVector) {
    Home(Icons.Outlined.Home),
    Spices(Icons.Outlined.ShoppingCart),
    LEDs(Icons.Outlined.AccountBox)
}