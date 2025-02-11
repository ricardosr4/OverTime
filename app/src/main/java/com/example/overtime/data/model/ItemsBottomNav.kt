import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

sealed class ItemsBottomNav(
    val icon: ImageVector,
    val title: String,
    val route: String
) {

    data object HomeScreen : ItemsBottomNav(
        Icons.Default.Home,
        "Home",
        "home_screen")
    data object ConfigScreen : ItemsBottomNav(
        Icons.Default.Settings,
        "Configuracion",
        "config_screen")
    data object AddHrsExtras : ItemsBottomNav(
        Icons.Default.Add,
        "Agregar hrs",
        "add_hrs_extras")

}
