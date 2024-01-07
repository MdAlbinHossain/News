package bd.com.albin.news.ui.screens.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import bd.com.albin.news.data.local.entities.ArticleEntity
import bd.com.albin.news.data.network.models.NetworkArticle
import bd.com.albin.news.data.network.models.asEntity
import bd.com.albin.news.domain.news.DeleteArticleUseCase
import bd.com.albin.news.domain.news.GetArticleIdsUseCase
import bd.com.albin.news.domain.news.GetNewsUseCase
import bd.com.albin.news.domain.news.SaveArticleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getNewsUseCase: GetNewsUseCase,
    private val saveArticleUseCase: SaveArticleUseCase,
    private val deleteArticleUseCase: DeleteArticleUseCase,
    getArticleIdsUseCase: GetArticleIdsUseCase
) : ViewModel() {
    var uiState by mutableStateOf(SearchUiState())
        private set

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

    fun updateUiState(query: String) {
        uiState = uiState.copy(query = query)
    }

    fun searchNews(query: String) {
        getNews(query)
    }

    private fun getNews(query: String) {
        val articles = getNewsUseCase(
            query = query,
        ).map {
            it.map(NetworkArticle::asEntity)
        }.cachedIn(viewModelScope)
        uiState = uiState.copy(articles = articles)
    }
}

data class SearchUiState(
    val query: String = "", val articles: Flow<PagingData<ArticleEntity>>? = null
)