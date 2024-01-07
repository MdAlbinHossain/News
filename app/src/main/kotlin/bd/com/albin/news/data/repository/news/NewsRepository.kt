package bd.com.albin.news.data.repository.news

import androidx.paging.PagingData
import bd.com.albin.news.data.local.entities.ArticleEntity
import bd.com.albin.news.data.network.models.NetworkArticle
import kotlinx.coroutines.flow.Flow


interface NewsRepository {
    fun getTopHeadlines(): Flow<PagingData<ArticleEntity>>

    fun getSavedNews(): Flow<PagingData<ArticleEntity>>

    fun searchNews(query: String): Flow<PagingData<NetworkArticle>>

    suspend fun getArticle(urlId: String): ArticleEntity

    suspend fun saveArticle(article: ArticleEntity)
    fun deleteArticle(article: ArticleEntity)

    fun getArticleIds(): Flow<List<String>>
}