package bd.com.albin.news.data.network.models

import kotlinx.serialization.Serializable

@Serializable
data class NetworkNews(
    val status: String,
    val totalResults: Int,
    val articles: List<NetworkArticle>,
)