package bd.com.albin.news.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import bd.com.albin.news.ui.NewsAppState
import bd.com.albin.news.ui.screens.article.ArticleDetailsScreen
import bd.com.albin.news.ui.common.ext.decodeUrl
import bd.com.albin.news.ui.common.ext.encodeUrl
import bd.com.albin.news.ui.common.ext.ensureHttpsUrl
import bd.com.albin.news.ui.screens.home.HomeScreen
import bd.com.albin.news.ui.screens.onboarding.OnboardingScreen
import bd.com.albin.news.ui.screens.onboarding.OnboardingViewModel
import bd.com.albin.news.ui.screens.search.SearchNewsViewModel
import bd.com.albin.news.ui.screens.search.SearchScreen

@Composable
fun NewsNavHost(
    appState: NewsAppState,
    startDestination: String,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = appState.navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {

        composable(route = NewsDestination.Onboarding.route) {
            val viewModel: OnboardingViewModel = hiltViewModel()
            OnboardingScreen(onAcceptClick = viewModel::saveUserConsent)
        }
        composable(route = NewsDestination.Home.route) {
            HomeScreen({ route: String ->
                appState.navigate("${NewsDestination.Article.route}/${route.encodeUrl()}")
            }, onNavigateToSearch = { appState.navigate(NewsDestination.Search.route) })
        }

        composable(
            NewsDestination.Article.routeWithArg,
            arguments = listOf(navArgument(NewsDestination.Article.URL_ARG) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            backStackEntry.arguments?.getString(NewsDestination.Article.URL_ARG)?.let {
                ArticleDetailsScreen(it.decodeUrl().ensureHttpsUrl(), appState::popUp)
            }
        }
        composable(route = NewsDestination.Search.route, enterTransition = {
            fadeIn(
                animationSpec = tween(
                    300, easing = LinearEasing
                )
            ) + slideIntoContainer(
                animationSpec = tween(300, easing = EaseIn),
                towards = AnimatedContentTransitionScope.SlideDirection.Up
            )
        }, exitTransition = {
            fadeOut(animationSpec = tween(300, easing = LinearEasing)) + slideOutOfContainer(
                animationSpec = tween(300, easing = LinearEasing),
                towards = AnimatedContentTransitionScope.SlideDirection.Down
            )
        }) {
            val viewModel = hiltViewModel<SearchNewsViewModel>()
            SearchScreen(
                viewModel.uiState, viewModel::updateUiState, viewModel::searchNews, appState::popUp
            ) { route: String ->
                appState.navigate("${NewsDestination.Article.route}/${route.encodeUrl()}")
            }
        }
    }
}