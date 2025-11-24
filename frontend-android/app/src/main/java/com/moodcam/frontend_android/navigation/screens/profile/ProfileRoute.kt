/** Registers the profile route (auth protected). */

package com.moodcam.frontend_android.navigation.screens.profile

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.moodcam.frontend_android.auth.vm.AuthViewModel
import com.moodcam.frontend_android.navigation.Routes
import com.moodcam.frontend_android.navigation.helpers.AuthorizedScreen
import com.moodcam.frontend_android.navigation.screens.profile.settingsRoute
import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.profileRoute(nav: NavHostController) {
    /**
     * Registers the profile screen route.
     * Provides callbacks for navigating to edit profile and settings.
     *
     * @param nav Host controller used for navigation actions.
     */
    composable(Routes.PROFILE) { navBackStackEntry ->
        val authViewModel: AuthViewModel = koinViewModel()
        
        AuthorizedScreen(authViewModel, nav) {
            ProfileScreenContent(
                navBackStackEntry = navBackStackEntry,
                onEditProfile = { nav.navigate(Routes.EDIT_PROFILE) },
                onSettingsClicked = { nav.navigate(Routes.SETTINGS) }
            )
        }
    }
}
