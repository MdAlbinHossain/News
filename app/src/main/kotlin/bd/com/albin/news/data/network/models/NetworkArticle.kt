package bd.com.albin.news.data.network.models

import bd.com.albin.news.data.local.entities.ArticleEntity
import bd.com.albin.news.data.model.Article
import kotlinx.serialization.Serializable

@Serializable
data class NetworkArticle(
    val source: NetworkSource,
    val author: String?,
    val title: String,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String?,
)

fun NetworkArticle.asEntity() = ArticleEntity(
    source = source.asLocal(),
    author = author,
    title = title,
    description = description,
    url = url,
    urlToImage = urlToImage,
    publishedAt = publishedAt,
    content = content ?: ""
)

fun NetworkArticle.asExternalModel() = Article(
    source = source.asExternal(),
    author = author,
    title = title,
    description = description,
    url = url,
    urlToImage = urlToImage,
    publishedAt = publishedAt,
    content = content?:"",
    timestamp = System.currentTimeMillis()
)