package bd.com.albin.news.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import bd.com.albin.news.data.local.entities.ArticleEntity
import bd.com.albin.news.domain.news.DeleteArticleUseCase
import bd.com.albin.news.domain.news.GetArticleIdsUseCase
import bd.com.albin.news.domain.news.GetSavedNewsUseCase
import bd.com.albin.news.domain.news.GetTopHeadlinesUseCase
import bd.com.albin.news.domain.news.SaveArticleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    getTopHeadlinesUseCase: GetTopHeadlinesUseCase, getSavedNewsUseCase: GetSavedNewsUseCase,
    getArticleIdsUseCase: GetArticleIdsUseCase,
    private val saveArticleUseCase: SaveArticleUseCase,
    private val deleteArticleUseCase: DeleteArticleUseCase,
) : ViewModel() {
    val headLinesUiState = getTopHeadlinesUseCase().cachedIn(viewModelScope)
    val savedNewsUiState = getSavedNewsUseCase().cachedIn(viewModelScope)

    val savedArticleIds: StateFlow<Set<String>> = getArticleIdsUseCase().map {
        it.toSet()
    }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5_000), emptySet()
    )

    fun saveArticle(articleEntity: ArticleEntity) = viewModelScope.launch {
        saveArticleUseCase(articleEntity)
    }

    fun deleteArticle(articleEntity: ArticleEntity) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            deleteArticleUseCase(articleEntity)
        }
    }
}