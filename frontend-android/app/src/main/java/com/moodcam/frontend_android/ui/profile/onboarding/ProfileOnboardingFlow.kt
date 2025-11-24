/** Coordinator for multi-step profile onboarding (welcome, age, name). */

package com.moodcam.frontend_android.ui.profile.onboarding

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

/** Onboarding steps. */
enum class OnboardingStep {
    WELCOME,  /** Welcome screen */
    AGE,      /** Age selection screen */
    NAME      /** Name input screen */
}

/** Renders current onboarding step and collects name + age.
 * @param modifier Optional modifier.
 * @param onComplete Callback with (name, age).
 */
@Composable
fun ProfileOnboardingFlow(
    modifier: Modifier = Modifier,
    onComplete: (name: String, age: Int) -> Unit
) {
    var currentStep by remember { mutableStateOf(OnboardingStep.WELCOME) }
    var selectedAge by remember { mutableStateOf(25) }
    var selectedName by remember { mutableStateOf("") }

    when (currentStep) {
        OnboardingStep.WELCOME -> {
            WelcomeOnboardingScreen(
                onNextClick = {
                    currentStep = OnboardingStep.AGE
                }
            )
        }
        OnboardingStep.AGE -> {
            AgeSelectionScreen(
                onNextClick = { age ->
                    selectedAge = age
                    currentStep = OnboardingStep.NAME
                }
            )
        }
        OnboardingStep.NAME -> {
            NameInputScreen(
                onCompleteClick = { name ->
                    selectedName = name
                    onComplete(selectedName, selectedAge)
                }
            )
        }
    }
}
