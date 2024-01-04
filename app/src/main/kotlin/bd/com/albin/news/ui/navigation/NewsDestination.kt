package bd.com.albin.news.ui.navigation

sealed class NewsDestination(val route: String) {
    data object Onboarding : NewsDestination(route = "onboarding")
    data object Home : NewsDestination(route = "home")
    data object Search : NewsDestination(route = "search")
    data object Article : NewsDestination(route = "article") {
        const val URL_ARG = "url"
        val routeWithArg = "$route/{$URL_ARG}"
    }
}