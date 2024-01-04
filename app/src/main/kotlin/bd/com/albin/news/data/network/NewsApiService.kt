package bd.com.albin.news.data.network

import bd.com.albin.news.BuildConfig
import bd.com.albin.news.data.network.models.NetworkNews
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {

    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY,
        @Query("country") country: String? = "us",
        @Query("category") category: String? = null,
        @Query("page") page: Int = 1,
        @Query("pageSize") pageSize: Int = 100,
    ): NetworkNews

    @GET("everything")
    suspend fun getNews(
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY,
        @Query("q") query: String = "",
        @Query("page") page: Int = 1,
        @Query("pageSize") pageSize: Int = 100,
    ): NetworkNews

}