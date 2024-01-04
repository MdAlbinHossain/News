package bd.com.albin.news.ui.screens.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import bd.com.albin.news.R
import bd.com.albin.news.data.local.entities.Source
import bd.com.albin.news.data.model.Article
import bd.com.albin.news.ui.common.composable.ErrorScreen
import bd.com.albin.news.ui.common.composable.LoadingScreen
import bd.com.albin.news.ui.common.ext.shimmerEffect
import bd.com.albin.news.ui.theme.NewsTheme
import coil.compose.AsyncImage
import kotlinx.datetime.Instant
import java.util.concurrent.TimeUnit

@Composable
fun SourceAndPublishTime(
    article: Article, modifier: Modifier = Modifier
) {

    var timeValue: Long = TimeUnit.MINUTES.convert(
        System.currentTimeMillis() - Instant.parse(article.publishedAt).toEpochMilliseconds(),
        TimeUnit.MILLISECONDS
    )
    var timeUnit = "minute"
    if (timeValue >= 60) {
        timeValue /= 60
        timeUnit = "hour"
    }
    if (timeUnit == "hour" && timeValue >= 24) {
        timeValue /= 24
        timeUnit = "day"
    }
    if (timeValue > 1) timeUnit += "s"

    Row(modifier) {
        Text(
            text = "$timeValue $timeUnit ago - " + article.source.name,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun ArticleImage(article: Article, modifier: Modifier = Modifier) {
    AsyncImage(
        model = article.urlToImage,
        contentDescription = null,
        placeholder = painterResource(id = R.drawable.newspaper),
        modifier = modifier
            .size(80.dp)
            .clip(MaterialTheme.shapes.medium),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun ArticleTitle(article: Article, modifier: Modifier = Modifier) {
    Text(
        text = article.title,
        style = MaterialTheme.typography.titleMedium,
        maxLines = 3,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
    )
}

@Composable
fun ArticleCard(
    article: Article,
    navigateToArticle: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .clickable(onClick = { navigateToArticle(article.url) })
            .padding(16.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ArticleTitle(article, modifier = Modifier.weight(1f))
            ArticleImage(
                article, Modifier
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SourceAndPublishTime(article)
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun ArticleCardShimmerEffect(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .height(24.dp)
                .fillMaxWidth()
                .shimmerEffect()
        )
        Box(
            modifier = Modifier
                .height(24.dp)
                .fillMaxWidth()
                .shimmerEffect()
        )
        Box(
            modifier = Modifier
                .height(24.dp)
                .fillMaxWidth(0.5f)
                .shimmerEffect()
        )
    }
}

@Composable
fun ArticleListDivider() {
    Divider(
        modifier = Modifier.padding(horizontal = 14.dp),
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ArticleList(
    articles: LazyPagingItems<Article>,
    navigateToArticle: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    val pullRefreshState =
        rememberPullRefreshState(refreshing = articles.loadState.refresh is LoadState.Loading,
            onRefresh = { articles.refresh() })

    Box(
        modifier = modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(Modifier.fillMaxSize()) {
            items(articles.itemCount) { index ->
                val article = articles[index]
                if (article != null) {
                    ArticleCard(
                        article = article,
                        navigateToArticle = navigateToArticle,
                    )
                    ArticleListDivider()
                }
            }
            if (articles.loadState.append is LoadState.Loading) item {
                LoadingScreen()
            }
            if (articles.loadState.append is LoadState.Error) item {
                ErrorScreen(message = "Loading Failed", retryAction = { articles.retry() })
            }
        }
        PullRefreshIndicator(
            refreshing = articles.loadState.refresh is LoadState.Loading,
            state = pullRefreshState,
            modifier = Modifier,
            backgroundColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewArticleImage() {
    NewsTheme {
        ArticleImage(article = Article())
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewArticleCard() {
    NewsTheme {
        ArticleCard(
            article = Article(
                Source("", "BBC News"),
                title = "This a news title very long news title - sample news title",
            )
        ) {}
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewArticleCardShimmerEffect() {
    NewsTheme {
        ArticleCardShimmerEffect()
    }
}