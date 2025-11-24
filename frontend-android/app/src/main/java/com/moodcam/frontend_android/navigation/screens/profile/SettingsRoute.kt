/** Registers the settings route (auth protected). */

package com.moodcam.frontend_android.navigation.screens.profile

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.moodcam.frontend_android.auth.vm.AuthViewModel
import com.moodcam.frontend_android.navigation.Routes
import com.moodcam.frontend_android.navigation.helpers.AuthorizedScreen
import com.moodcam.frontend_android.ui.settings.SettingsScreen
import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.settingsRoute(nav: NavHostController) {
    /**
     * Registers the settings screen route.
     * Presents configurable application options to authenticated users.
     *
     * @param nav Host controller used for navigation actions.
     */
    composable(Routes.SETTINGS) {
        val authViewModel: AuthViewModel = koinViewModel()

        AuthorizedScreen(authViewModel, nav) {
            SettingsScreen(
                onNavigateUp = {
                    nav.navigateUp()
                }
            )
        }
    }
}
