/**
 * @file AuthScreenLayout.kt
 * @brief Layout wrapper for authentication screens
 */

package com.moodcam.frontend_android.auth.ui.elements
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AuthScreenLayout(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    /**
     * Root layout for auth screens providing centered branding header and form content.
     *
     * @param modifier Optional modifier for the outer column.
     * @param content Form composable scope rendered beneath the title.
     */
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "MOODCAM",
            modifier = Modifier.padding(bottom = 32.dp)
        )
        content()
    }
}