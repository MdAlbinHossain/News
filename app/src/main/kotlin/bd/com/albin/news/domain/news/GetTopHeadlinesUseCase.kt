package bd.com.albin.news.domain.news

import androidx.paging.PagingData
import bd.com.albin.news.data.local.entities.ArticleEntity
import bd.com.albin.news.data.repository.news.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTopHeadlinesUseCase @Inject constructor(private val newsRepository: NewsRepository) {
    operator fun invoke(): Flow<PagingData<ArticleEntity>> {
        return newsRepository.getTopHeadlines()
    }
}