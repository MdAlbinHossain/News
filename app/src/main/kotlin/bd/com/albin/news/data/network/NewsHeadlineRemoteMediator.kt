package bd.com.albin.news.data.network

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import bd.com.albin.news.data.local.cache.NewsCacheDatabase
import bd.com.albin.news.data.local.entities.ArticleEntity
import bd.com.albin.news.data.network.models.NetworkArticle
import bd.com.albin.news.data.network.models.asEntity
import okio.IOException
import retrofit2.HttpException
import java.util.concurrent.TimeUnit
import kotlin.math.min

@OptIn(ExperimentalPagingApi::class)
class NewsHeadlineRemoteMediator(
    private val newsCacheDatabase: NewsCacheDatabase, private val newsApiService: NewsApiService
) : RemoteMediator<Int, ArticleEntity>() {
    private var articleCounter = 0
    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, ArticleEntity>
    ): MediatorResult {
        return try {
            val loadKey: Int = when (loadType) {
                LoadType.REFRESH -> {
                    articleCounter = 0
                    1
                }

                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)

                LoadType.APPEND -> {
                    newsCacheDatabase.withTransaction {
                        articleCounter = newsCacheDatabase.articleCacheDao.getTotalCount() ?: 0
                    }
                    (articleCounter / state.config.pageSize) + 1
                }
            }

            val response = newsApiService.getTopHeadlines(apiKey = apiKeys.random(),
                page = loadKey, pageSize = state.config.pageSize
            )

            val articles = response.articles.distinctBy { it.title }


            newsCacheDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    newsCacheDatabase.articleCacheDao.clearAll()
                }

                articleCounter += articles.size
                newsCacheDatabase.articleCacheDao.insertAll(articles.map(NetworkArticle::asEntity))
            }

            MediatorResult.Success(
                endOfPaginationReached = min(100, response.totalResults) == articleCounter
            )
        } catch (
            e: IOException
        ) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)
        val lastUpdated = newsCacheDatabase.withTransaction {
            newsCacheDatabase.articleCacheDao.getAnyOneItem()?.timestamp ?: 0L
        }
        return if (System.currentTimeMillis() - lastUpdated <= cacheTimeout) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

}
