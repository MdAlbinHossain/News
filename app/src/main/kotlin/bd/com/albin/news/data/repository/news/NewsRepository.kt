package bd.com.albin.news.data.repository.news

import androidx.paging.PagingData
import bd.com.albin.news.data.local.entities.ArticleEntity
import bd.com.albin.news.data.network.models.NetworkArticle
import kotlinx.coroutines.flow.Flow


interface NewsRepository {
    fun getTopHeadlines(): Flow<PagingData<ArticleEntity>>
    fun getNews(query: String): Flow<PagingData<NetworkArticle>>
}