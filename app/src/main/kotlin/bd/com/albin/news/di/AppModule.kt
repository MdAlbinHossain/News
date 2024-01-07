package bd.com.albin.news.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import bd.com.albin.news.BuildConfig
import bd.com.albin.news.data.local.cache.NewsCacheDatabase
import bd.com.albin.news.data.local.saved.SavedArticleDatabase
import bd.com.albin.news.data.network.NewsApiService
import bd.com.albin.news.data.repository.news.NewsRepository
import bd.com.albin.news.data.repository.news.NewsRepositoryImpl
import bd.com.albin.news.data.repository.user.UserDataRepository
import bd.com.albin.news.data.repository.user.UserDataRepositoryImpl
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Singleton


private const val USER_PREFERENCES_NAME = "user_preferences"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = USER_PREFERENCES_NAME
)

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providesUserPreferencesRepository(@ApplicationContext context: Context): UserDataRepository =
        UserDataRepositoryImpl(context.dataStore)

    @Provides
    @Singleton
    fun providesRetrofit(): Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BuildConfig.BASE_URL).build()

    @Provides
    @Singleton
    fun providesNewsApiService(retrofit: Retrofit): NewsApiService =
        retrofit.create(NewsApiService::class.java)

    @Provides
    @Singleton
    fun providesNewsCacheDatabase(@ApplicationContext context: Context): NewsCacheDatabase =
        Room.databaseBuilder(
            context = context, klass = NewsCacheDatabase::class.java, name = "news_cache_db"
        ).fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun providesSavedArticleDatabase(@ApplicationContext context: Context): SavedArticleDatabase =
        Room.databaseBuilder(
            context = context, klass = SavedArticleDatabase::class.java, name = "saved_article_db"
        ).fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun providesNewsRepository(
        newsApiService: NewsApiService,
        newsCacheDatabase: NewsCacheDatabase,
        savedArticleDatabase: SavedArticleDatabase
    ): NewsRepository =
        NewsRepositoryImpl(newsApiService, newsCacheDatabase, savedArticleDatabase.savedArticleDao)
}