package bd.com.albin.news.data.local.cache

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import bd.com.albin.news.data.local.entities.ArticleEntity

@Dao
interface ArticleCacheDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(articles: List<ArticleEntity>)

    @Query("SELECT * FROM ArticleEntity WHERE title IS NOT \'[Removed]\' AND url_to_image IS NOT null ORDER BY published_at DESC")
    fun pagingSource(): PagingSource<Int, ArticleEntity>

    @Query("SELECT * FROM ArticleEntity WHERE url = :url")
    fun getArticle(url: String): ArticleEntity

    @Query("SELECT * FROM ArticleEntity LIMIT 1")
    fun getAnyOneItem(): ArticleEntity?

    @Query("SELECT COUNT(*) FROM ArticleEntity")
    fun getTotalCount(): Int?

    @Query("DELETE FROM ArticleEntity")
    suspend fun clearAll()
}