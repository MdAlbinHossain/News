package bd.com.albin.news.domain.news

import bd.com.albin.news.data.local.entities.ArticleEntity
import bd.com.albin.news.data.repository.news.NewsRepository
import javax.inject.Inject

class GetArticleUseCase @Inject constructor(private val repository: NewsRepository) {
    suspend operator fun invoke(urlId: String): ArticleEntity = repository.getArticle(urlId)
}