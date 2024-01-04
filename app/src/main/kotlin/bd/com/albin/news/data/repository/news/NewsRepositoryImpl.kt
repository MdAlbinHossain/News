package bd.com.albin.news.data.repository.news

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import bd.com.albin.news.data.local.NewsDatabase
import bd.com.albin.news.data.local.entities.ArticleEntity
import bd.com.albin.news.data.network.NewsApiService
import bd.com.albin.news.data.network.NewsHeadlineRemoteMediator
import bd.com.albin.news.data.network.NewsPagingSource
import bd.com.albin.news.data.network.models.NetworkArticle
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newsApiService: NewsApiService, private val newsDatabase: NewsDatabase
) : NewsRepository {
    @OptIn(ExperimentalPagingApi::class)
    override fun getTopHeadlines(): Flow<PagingData<ArticleEntity>> {
        val newsDao = newsDatabase.articleDao
        return Pager(
            config = PagingConfig(pageSize = 100),
            remoteMediator = NewsHeadlineRemoteMediator(newsDatabase, newsApiService)
        ) {
            newsDao.pagingSource()
        }.flow
    }

    override fun getNews(query: String): Flow<PagingData<NetworkArticle>> {
        return Pager(config = PagingConfig(pageSize = 100), pagingSourceFactory = {
            NewsPagingSource(query = query, newsApiService = newsApiService)
        }).flow
    }
}