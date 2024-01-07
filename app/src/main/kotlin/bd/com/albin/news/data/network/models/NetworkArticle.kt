package bd.com.albin.news.data.network.models

import bd.com.albin.news.data.local.entities.ArticleEntity
import bd.com.albin.news.data.local.entities.asExternalModel
import bd.com.albin.news.data.model.Article
import bd.com.albin.news.ui.common.ext.encodeUrl
import kotlinx.serialization.Serializable

@Serializable
data class NetworkArticle(
    val source: NetworkSource= NetworkSource(),
    val author: String?="",
    val title: String="",
    val description: String?="",
    val url: String="",
    val urlToImage: String?="",
    val publishedAt: String="2024-01-01T00:00:00.475Z",
    val content: String?="",
)

fun NetworkArticle.asEntity() = ArticleEntity(
    urlId = url.encodeUrl(),
    source = source.asLocal(),
    author = author,
    title = title,
    description = description,
    url = url,
    urlToImage = urlToImage,
    publishedAt = publishedAt,
    content = content ?: ""
)

fun NetworkArticle.asExternalModel(): Article = this.asEntity().asExternalModel()
