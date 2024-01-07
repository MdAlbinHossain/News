package bd.com.albin.news.data.local.saved

import androidx.room.Database
import androidx.room.RoomDatabase
import bd.com.albin.news.data.local.entities.ArticleEntity

@Database(entities = [ArticleEntity::class], version = 1, exportSchema = false)
abstract class SavedArticleDatabase : RoomDatabase() {

    abstract val savedArticleDao: SavedArticleDao

}