package bd.com.albin.news.ui

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import bd.com.albin.news.ui.navigation.NewsNavHost

@Composable
fun NewsApp(
    startDestination: String,
    windowSizeClass: WindowSizeClass,
    appState: NewsAppState = rememberNewsAppState(windowSizeClass = windowSizeClass),
) {
    NewsNavHost(appState = appState, startDestination = startDestination)
}