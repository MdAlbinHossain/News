package bd.com.albin.news.ui.screens.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import bd.com.albin.news.data.model.Article
import bd.com.albin.news.data.network.models.NetworkArticle
import bd.com.albin.news.data.network.models.asExternalModel
import bd.com.albin.news.domain.news.GetNewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


@HiltViewModel
class SearchNewsViewModel @Inject constructor(private val getNewsUseCase: GetNewsUseCase) :
    ViewModel() {
    var uiState by mutableStateOf(SearchUiState())
        private set

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
            it.map(NetworkArticle::asExternalModel)
        }.cachedIn(viewModelScope)
        uiState = uiState.copy(articles = articles)
    }
}

data class SearchUiState(
    val query: String = "", val articles: Flow<PagingData<Article>>? = null
)