package bd.com.albin.news.domain.news

import androidx.paging.PagingData
import bd.com.albin.news.data.network.models.NetworkArticle
import bd.com.albin.news.data.repository.news.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNewsUseCase @Inject constructor(private val newsRepository: NewsRepository) {
    operator fun invoke(query: String): Flow<PagingData<NetworkArticle>> {
        return newsRepository.getNews(query)
    }
}