package bd.com.albin.news.domain.news

import bd.com.albin.news.data.repository.news.NewsRepository
import javax.inject.Inject

class GetArticleIdsUseCase @Inject constructor(private val repository: NewsRepository) {
    operator fun invoke() = repository.getArticleIds()
}