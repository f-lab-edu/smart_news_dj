package com.djyoo.smartnews

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.djyoo.smartnews.presentation.navigation.AppNavigation
import com.djyoo.smartnews.presentation.theme.SmartNewsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartNewsTheme {
                AppNavigation()
            }
        }
    }
}
