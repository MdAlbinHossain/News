package bd.com.albin.news.ui.screens.search

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.compose.collectAsLazyPagingItems
import bd.com.albin.news.ui.screens.home.components.ArticleList
import bd.com.albin.news.ui.theme.NewsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    uiState: SearchUiState,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onNavigateBack: () -> Unit,
    navigateToArticle: (String) -> Unit,
) {

    val focusRequester = remember {
        FocusRequester()
    }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    SearchBar(query = uiState.query,
        onQueryChange = onQueryChange,
        onSearch = onSearch,
        active = true,
        onActiveChange = { },
        leadingIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                )
            }
        },
        placeholder = { Text(text = "Search for topics, locations & sources") },
        modifier = Modifier.focusRequester(focusRequester)
    ) {
        uiState.articles?.let {
            ArticleList(
                articles = it.collectAsLazyPagingItems(), navigateToArticle = navigateToArticle
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewSearchScreen() {
    NewsTheme {
        SearchScreen(SearchUiState(), {}, {}, {}) {}
    }
}