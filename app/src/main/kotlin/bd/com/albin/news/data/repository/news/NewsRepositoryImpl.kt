package bd.com.albin.news.data.repository.news

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import bd.com.albin.news.data.local.cache.NewsCacheDatabase
import bd.com.albin.news.data.local.entities.ArticleEntity
import bd.com.albin.news.data.network.NewsApiService
import bd.com.albin.news.data.network.NewsHeadlineRemoteMediator
import bd.com.albin.news.data.network.NewsSearchPagingSource
import bd.com.albin.news.data.network.models.NetworkArticle
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newsApiService: NewsApiService,
    private val newsCacheDatabase: NewsCacheDatabase,
) : NewsRepository {

    private val newsCacheDao = newsCacheDatabase.articleCacheDao

    @OptIn(ExperimentalPagingApi::class)
    override fun getTopHeadlines(): Flow<PagingData<ArticleEntity>> {
        return Pager(
            config = PagingConfig(pageSize = 100),
            remoteMediator = NewsHeadlineRemoteMediator(newsCacheDatabase, newsApiService)
        ) {
            newsCacheDao.pagingSource()
        }.flow
    }

    override fun searchNews(query: String): Flow<PagingData<NetworkArticle>> {
        return Pager(config = PagingConfig(pageSize = 100), pagingSourceFactory = {
            NewsSearchPagingSource(query = query, newsApiService = newsApiService)
        }).flow
    }
}