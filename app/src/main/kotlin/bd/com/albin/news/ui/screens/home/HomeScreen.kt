package bd.com.albin.news.ui.screens.home

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ViewHeadline
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import bd.com.albin.news.R
import bd.com.albin.news.data.local.entities.ArticleEntity
import bd.com.albin.news.ui.common.composable.ErrorScreen
import bd.com.albin.news.ui.screens.home.components.ArticleCardShimmerEffect
import bd.com.albin.news.ui.screens.home.components.ArticleList

enum class NewsTab {
    Headlines, SavedNews,
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    currentTab: MutableState<NewsTab>,
    onNavigateToArticle: (String) -> Unit,
    onNavigateToSearch: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {

    val articles = viewModel.headLinesUiState.collectAsLazyPagingItems()
    val savedArticles = viewModel.savedNewsUiState.collectAsLazyPagingItems()
    val savedArticleIds: Set<String> by viewModel.savedArticleIds.collectAsStateWithLifecycle()

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(modifier = Modifier
        .nestedScroll(scrollBehavior.nestedScrollConnection)
        .windowInsetsPadding(
            WindowInsets.navigationBars
        ), topBar = {
        CenterAlignedTopAppBar(
            title = { Text(text = stringResource(id = if (currentTab.value == NewsTab.Headlines) R.string.app_name else R.string.saved_news)) },
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
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                }
            },
            scrollBehavior = scrollBehavior
        )
    }, bottomBar = {
        NavigationBar {
            NavigationBarItem(icon = {
                Icon(
                    Icons.Filled.ViewHeadline, contentDescription = null
                )
            },
                label = { Text(stringResource(R.string.headlines)) },
                selected = currentTab.value == NewsTab.Headlines,
                onClick = { currentTab.value = NewsTab.Headlines })
            NavigationBarItem(icon = { Icon(Icons.Filled.Bookmark, contentDescription = null) },
                label = { Text(stringResource(R.string.saved_news)) },
                selected = currentTab.value == NewsTab.SavedNews,
                onClick = { currentTab.value = NewsTab.SavedNews })
        }
    }) { innerPadding ->
        when (currentTab.value) {
            NewsTab.Headlines -> {
                HomeBody(
                    articles = articles,
                    savedArticleIds,
                    viewModel::saveArticle,
                    viewModel::deleteArticle,
                    onNavigateToArticle,
                    modifier = Modifier.padding(innerPadding)
                )
            }

            NewsTab.SavedNews -> {

                if (savedArticles.itemCount == 0) {
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bookmarks,
                            contentDescription = null,
                            modifier = Modifier.size(128.dp)
                        )
                        Text(
                            text = stringResource(R.string.no_saved_news_yet_message),
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    HomeBody(
                        articles = savedArticles,
                        savedArticleIds,
                        viewModel::saveArticle,
                        viewModel::deleteArticle,
                        onNavigateToArticle,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

}

@Composable
fun HomeBody(
    articles: LazyPagingItems<ArticleEntity>,
    savedArticleIds: Set<String>,
    onSaveArticle: (ArticleEntity) -> Unit,
    onDeleteArticle: (ArticleEntity) -> Unit,
    onNavigateToArticle: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Crossfade(
        targetState = articles.loadState.refresh,
        label = "Initial Loading",
        animationSpec = tween(500)
    ) {
        when (it) {
            LoadState.Loading -> {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp), modifier = modifier
                ) {
                    repeat(10) {
                        ArticleCardShimmerEffect(
                            modifier = Modifier
                        )
                    }
                }
            }

            is LoadState.Error -> ErrorScreen(message = stringResource(R.string.load_error_message),
                retryAction = { articles.refresh() })

            else -> {
                ArticleList(
                    articles = articles,
                    savedArticleIds,
                    onSaveArticle,
                    onDeleteArticle,
                    navigateToArticle = onNavigateToArticle,
                    modifier = modifier
                )
            }
        }
    }
}