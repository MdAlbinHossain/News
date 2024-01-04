package bd.com.albin.news.ui.screens.article

import android.graphics.Bitmap
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import bd.com.albin.news.ui.theme.NewsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleDetailsScreen(articleUrl: String, onNavigateBack: () -> Unit) {
    Scaffold(topBar = {
        TopAppBar(title = {}, navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
        })
    }) {
        Column(modifier = Modifier.padding(it)) {
            ArticleWebView(
                url = articleUrl, modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun ArticleWebView(url: String, modifier: Modifier = Modifier) {
    var isLoading by remember { mutableStateOf(true) }
    var backEnabled by remember { mutableStateOf(false) }
    var webView: WebView? = null
    Column (modifier = modifier){
       if(isLoading) LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
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
        ArticleDetailsScreen("", onNavigateBack = {})
    }
}