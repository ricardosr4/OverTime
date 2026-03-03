import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.overtime.R

sealed class ItemsBottomNav(
    val icon: ImageVector,
    @StringRes val labelResId: Int,
    val route: String
) {

    data object HomeScreen : ItemsBottomNav(
        icon = Icons.Filled.Home,
        labelResId = R.string.bottom_home,
        route = "home_screen"
    )

    data object ConfigScreen : ItemsBottomNav(
        icon = Icons.Filled.Settings,
        labelResId = R.string.bottom_settings,
        route = "config_screen"
    )

    data object AddHrsExtras : ItemsBottomNav(
        icon = Icons.Filled.Add,
        labelResId = R.string.bottom_add,
        route = "add_hrs_extras"
    )
}
