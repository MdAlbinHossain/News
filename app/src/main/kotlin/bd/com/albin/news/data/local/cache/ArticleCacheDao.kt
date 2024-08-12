package bd.com.albin.news.data.local.cache

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import bd.com.albin.news.data.local.entities.ArticleEntity

@Dao
interface ArticleCacheDao {
    @Upsert
    suspend fun insertAll(articles: List<ArticleEntity>)

    @Query("SELECT * FROM ArticleEntity")
    fun pagingSource(): PagingSource<Int, ArticleEntity>

    @Query("SELECT * FROM ArticleEntity WHERE url_id = :urlId")
    fun getArticle(urlId: String): ArticleEntity?

    @Query("SELECT * FROM ArticleEntity LIMIT 1")
    fun getAnyOneItem(): ArticleEntity?

    @Query("SELECT COUNT(*) FROM ArticleEntity")
    fun getTotalCount(): Int?

    @Query("DELETE FROM ArticleEntity")
    suspend fun clearAll()
}