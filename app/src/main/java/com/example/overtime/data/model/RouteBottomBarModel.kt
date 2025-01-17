import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

sealed class RouteBottomBarModel(
    val icon: ImageVector,
    val title: String,
    val route: String
) {

    data object HomeScreen : RouteBottomBarModel(Icons.Default.Home, "home", "home_screen")
    data object ConfigScreen : RouteBottomBarModel(Icons.Default.Settings, "config", "config_screen")
    data object AddHrsExtras : RouteBottomBarModel(Icons.Default.Add, "add_hrs", "add_hrs_extras")
}
