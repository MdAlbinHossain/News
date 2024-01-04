package bd.com.albin.news.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import bd.com.albin.news.data.local.entities.ArticleEntity

@Database(entities = [ArticleEntity::class], version = 2, exportSchema = false)
abstract class NewsDatabase : RoomDatabase() {

    abstract val articleDao: ArticleDao

}