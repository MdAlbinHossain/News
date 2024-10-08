package bd.com.albin.news.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import bd.com.albin.news.data.model.Article

@Entity
data class ArticleEntity(
    @PrimaryKey @ColumnInfo(name = "url_id") val urlId: String,
    @Embedded val source: Source,
    val author: String?,
    val title: String,
    val description: String?,
    val url: String,
    @ColumnInfo(name = "url_to_image") val urlToImage: String?,
    @ColumnInfo(name = "published_at") val publishedAt: String,
    val content: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

fun ArticleEntity.asExternalModel() = Article(
    id = urlId,
    source = source,
    author = author,
    title = title,
    description = description,
    url = url,
    urlToImage = urlToImage,
    publishedAt = publishedAt,
    content = content,
    timestamp = timestamp
)