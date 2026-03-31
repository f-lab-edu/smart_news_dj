package com.djyoo.smartnews.presentation.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.djyoo.smartnews.presentation.detail.NewsDetailScreen
import com.djyoo.smartnews.presentation.home.HomeScreen

object Routes {
    const val HOME = "home"
    const val DETAIL = "detail?articleId={articleId}"

    fun detailRoute(articleId: String): String = "detail?articleId=${Uri.encode(articleId)}"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Routes.HOME,
    ) {
        composable(Routes.HOME) {
            HomeScreen(
                onOpenDetail = { articleId ->
                    navController.navigate(Routes.detailRoute(articleId))
                },
            )
        }
        composable(
            route = Routes.DETAIL,
            arguments = listOf(navArgument("articleId") { type = NavType.StringType }),
        ) {
            NewsDetailScreen(onBack = { navController.popBackStack() })
        }
    }
}
