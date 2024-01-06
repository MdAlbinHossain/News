package bd.com.albin.news.ui.screens.article

import android.content.Intent
import android.graphics.Bitmap
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Web
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import bd.com.albin.news.R
import bd.com.albin.news.ui.common.ext.ensureHttpsUrl
import bd.com.albin.news.ui.theme.NewsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleDetailsScreen(
    articleUrl: String,
    onNavigateBack: () -> Unit
) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, articleUrl)
    }
    val localContext = LocalContext.current
    val uriHandler = LocalUriHandler.current

    Scaffold(topBar = {
        CenterAlignedTopAppBar(title = {
            OutlinedButton(onClick = {
                uriHandler.openUri(articleUrl)
            }) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(imageVector = Icons.Default.Web, contentDescription = null)
                    Text(stringResource(R.string.view_original_source))
                }
            }
        }, navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
        }, actions = {

            IconButton(onClick = {
                localContext.startActivity(
                    Intent.createChooser(
                        shareIntent, "Share with"
                    )
                )
            }) {
                Icon(imageVector = Icons.Filled.Share, contentDescription = null)
            }
        })
    }) {
        ArticleWebView(
            url = articleUrl.ensureHttpsUrl(), modifier = Modifier
                .fillMaxSize()
                .padding(it)
        )
    }
}

@Composable
fun ArticleWebView(url: String, modifier: Modifier = Modifier) {
    var isLoading by remember { mutableStateOf(true) }
    var backEnabled by remember { mutableStateOf(false) }
    var webView: WebView? = null
    Column(modifier = modifier) {
        if (isLoading) LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        AndroidView(modifier = Modifier.weight(1f), factory = { context ->
            WebView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
                )
                webViewClient = object : WebViewClient() {
                    override fun onPageStarted(view: WebView, url: String?, favicon: Bitmap?) {
                        super.onPageStarted(view, url, favicon)
                        backEnabled = view.canGoBack()
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        isLoading = false
                    }
                }
                loadUrl(url)
                webView = this
            }
        }, update = {
            webView = it
        })
    }


    BackHandler(enabled = backEnabled) {
        webView?.goBack()
    }
}


@Preview
@Composable
fun PreviewArticleWebView() {
    NewsTheme {
        ArticleDetailsScreen("",  onNavigateBack = {},)
    }
}