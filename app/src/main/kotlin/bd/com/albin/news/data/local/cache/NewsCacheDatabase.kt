package bd.com.albin.news.data.local.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import bd.com.albin.news.data.local.entities.ArticleEntity

@Database(entities = [ArticleEntity::class], version = 2, exportSchema = false)
abstract class NewsCacheDatabase : RoomDatabase() {

    abstract val articleCacheDao: ArticleCacheDao

}