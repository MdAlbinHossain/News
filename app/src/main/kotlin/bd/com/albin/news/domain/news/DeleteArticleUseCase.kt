package bd.com.albin.news.domain.news

import bd.com.albin.news.data.local.entities.ArticleEntity
import bd.com.albin.news.data.repository.news.NewsRepository
import javax.inject.Inject

class DeleteArticleUseCase @Inject constructor(private val repository: NewsRepository) {
    operator fun invoke(articleEntity: ArticleEntity) =
        repository.deleteArticle(articleEntity)
}