package com.dev.hanji

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.ui.graphics.vector.ImageVector

interface HanjiDestination {
    val icon: ImageVector
    val route: String
    val title: String
}

object Home : HanjiDestination {
    override val icon: ImageVector = Icons.Filled.Home
    override val route: String = "home"
    override val title: String = "Home"

}
object Packs : HanjiDestination {
    override val icon: ImageVector = Icons.Filled.Favorite
    override val route: String = "packs"
    override val title: String = "Packs"
}
object User : HanjiDestination {
    override val icon: ImageVector = Icons.Filled.AccountCircle
    override val route: String = "user"
    override val title: String = "User"
}
object About : HanjiDestination {
    override val icon: ImageVector = Icons.Filled.Info
    override val route: String = "about"
    override val title: String = "About"
}

object Settings : HanjiDestination {
    override val icon: ImageVector = Icons.Filled.Settings
    override val route: String = "settings"
    override val title: String = "Settings"
}

object UserStats : HanjiDestination {
    override val route: String = "user_info"
    override val icon: ImageVector = Icons.Filled.AccountCircle
    override val title: String = "Info"
}

object UserAchievements : HanjiDestination {
    override val route: String = "user_achievements"
    override val icon: ImageVector = Icons.Filled.Star
    override val title: String = "Achievements"
}

object Draw : HanjiDestination {
    override val route: String = "draw_screen"
    override val icon: ImageVector = Icons.Outlined.Create
    override val title: String = "Draw"
}

object AllPacks : HanjiDestination {
    override val route: String = "packs_screen"
    override val icon: ImageVector = Icons.Outlined.DateRange
    override val title: String = "Packs"
}

object MyPacks : HanjiDestination {
    override val route: String = "my_packs_screen"
    override val icon: ImageVector = Icons.Outlined.Favorite
    override val title: String = "My Packs"
}

object CreatePack : HanjiDestination {
    override val route: String = "create_pack"
    override val icon: ImageVector = Icons.Filled.Add
    override val title: String = "Create Pack"
}


object EditPack: HanjiDestination {
    override val route: String = "edit_pack"
    override val icon: ImageVector = Icons.Filled.Edit
    override val title: String = "Edit pack"
}

object PackDetail: HanjiDestination {
    override val route: String = "pack_detail"
    override val icon: ImageVector = Icons.Filled.Info
    override val title: String = "Detail of pack"
}

object KanjiAll : HanjiDestination {
    override val route: String = "kanji_all"
    override val icon: ImageVector = Icons.Filled.Menu
    override val title: String = "All Kanji"
}

object KanjiAttempts : HanjiDestination {
    override val route: String = "kanji_attempts"
    override val icon: ImageVector = Icons.Filled.Info
    override val title: String = "Kanji user attempts"
}

object UserAttemptsKanji : HanjiDestination {
    override val route: String = "user_kanji_attempts"
    override val icon: ImageVector = Icons.Filled.Info
    override val title: String = "Attempts"
}

val hanjiScreens = listOf(Home, Packs, User, KanjiAll, About, Settings)
val userScreens = listOf(UserStats, UserAchievements, UserAttemptsKanji)
val packScreens = listOf(AllPacks, MyPacks)
val arrowScreens = listOf(CreatePack, PackDetail, EditPack)