package bd.com.albin.news.data.network

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import bd.com.albin.news.data.local.NewsDatabase
import bd.com.albin.news.data.local.entities.ArticleEntity
import bd.com.albin.news.data.network.models.NetworkArticle
import bd.com.albin.news.data.network.models.asEntity
import okio.IOException
import retrofit2.HttpException
import java.util.concurrent.TimeUnit
import kotlin.math.min

@OptIn(ExperimentalPagingApi::class)
class NewsHeadlineRemoteMediator(
    private val newsDatabase: NewsDatabase, private val newsApiService: NewsApiService
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
                    newsDatabase.withTransaction {
                        articleCounter = newsDatabase.articleDao.getTotalCount() ?: 0
                    }
                    (articleCounter / state.config.pageSize) + 1
                }
            }

            val response = newsApiService.getTopHeadlines(
                page = loadKey, pageSize = state.config.pageSize
            )

            val articles = response.articles.distinctBy { it.title }


            newsDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    newsDatabase.articleDao.clearAll()
                }

                articleCounter += articles.size
                newsDatabase.articleDao.insertAll(articles.map(NetworkArticle::asEntity))
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
        val lastUpdated = newsDatabase.withTransaction {
            newsDatabase.articleDao.getAnyOneItem()?.timestamp ?: 0L
        }
        return if (System.currentTimeMillis() - lastUpdated <= cacheTimeout) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

}
