package bd.com.albin.news.data.model

import bd.com.albin.news.data.local.entities.Source

data class Article(
    val source: Source = Source(),
    val author: String? = "",
    val title: String = "",
    val description: String? = "",
    val url: String = "",
    val urlToImage: String? = "",
    val publishedAt: String = "2024-01-01T00:00:00.475Z",
    val content: String = "",
    val timestamp: Long = System.currentTimeMillis(),
)