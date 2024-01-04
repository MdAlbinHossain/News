package bd.com.albin.news.ui.screens.home

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import bd.com.albin.news.R
import bd.com.albin.news.data.model.Article
import bd.com.albin.news.ui.common.composable.ErrorScreen
import bd.com.albin.news.ui.screens.home.components.ArticleCardShimmerEffect
import bd.com.albin.news.ui.screens.home.components.ArticleList

@Composable
fun HomeScreen(
    onNavigateToArticle: (String) -> Unit,
    onNavigateToSearch: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val articles = viewModel.uiState.collectAsLazyPagingItems()
    HomeBody(articles = articles, onNavigateToArticle, onNavigateToSearch, modifier = Modifier)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeBody(
    articles: LazyPagingItems<Article>,
    onNavigateToArticle: (String) -> Unit,
    onNavigateToSearch: () -> Unit,
    modifier: Modifier = Modifier
) {

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection), topBar = {
        CenterAlignedTopAppBar(title = { Text(text = stringResource(id = R.string.app_name)) },
            navigationIcon = {
                IconButton(onClick = onNavigateToSearch) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        modifier = Modifier.size(32.dp)
                    )
                }
            },
            actions = {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Outlined.AccountCircle,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                }
            },
            scrollBehavior = scrollBehavior
        )
    }) { innerPadding ->

        Crossfade(
            targetState = articles.loadState.refresh,
            label = "Initial Loading",
            animationSpec = tween(500)
        ) {
            when (it) {
                LoadState.Loading -> {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = modifier.padding(innerPadding)
                    ) {
                        repeat(10) {
                            ArticleCardShimmerEffect(
                                modifier = Modifier
                            )
                        }
                    }
                }

                is LoadState.Error -> ErrorScreen(message = "Error: Unable to load top headlines. Check your internet connection.",
                    retryAction = { articles.refresh() })

                else -> {
                    ArticleList(
                        articles = articles,
                        navigateToArticle = onNavigateToArticle,
                        modifier = modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}