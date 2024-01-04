package bd.com.albin.news.data.network

import androidx.paging.PagingSource
import androidx.paging.PagingState
import bd.com.albin.news.data.network.models.NetworkArticle
import kotlin.math.min

class NewsPagingSource(private val newsApiService: NewsApiService, private val query: String) :
    PagingSource<Int, NetworkArticle>() {
    override fun getRefreshKey(state: PagingState<Int, NetworkArticle>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    private var totalNewsCount = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NetworkArticle> {
        val page = params.key ?: 1
        return try {
            val newsResponse = newsApiService.getNews(query = query, page = page)
            totalNewsCount += newsResponse.articles.size

            val articles = newsResponse.articles.distinctBy { it.title }

            LoadResult.Page(
                data = articles, nextKey = if (totalNewsCount == min(
                        newsResponse.totalResults, 100
                    )
                ) null else page + 1, prevKey = null
            )
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(
                throwable = e
            )
        }
    }

}