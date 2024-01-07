package bd.com.albin.news.data.local.saved

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import bd.com.albin.news.data.local.entities.ArticleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: ArticleEntity)

    @Query("SELECT * FROM ArticleEntity WHERE title IS NOT \'[Removed]\' AND url_to_image IS NOT null ORDER BY published_at DESC")
    fun pagingSource(): PagingSource<Int, ArticleEntity>

    @Query("SELECT * FROM ArticleEntity WHERE url_id = :id")
    fun getArticle(id: String): ArticleEntity

    @Delete
    fun deleteArticle(article: ArticleEntity)

    @Query("SELECT url_id FROM ArticleEntity")
    fun getArticleIds(): Flow<List<String>>

    @Query("DELETE FROM ArticleEntity")
    suspend fun clearAll()
}