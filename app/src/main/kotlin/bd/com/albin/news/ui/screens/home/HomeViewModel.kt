package bd.com.albin.news.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import bd.com.albin.news.data.local.entities.ArticleEntity
import bd.com.albin.news.data.local.entities.asExternalModel
import bd.com.albin.news.domain.news.GetTopHeadlinesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    getTopHeadlinesUseCase: GetTopHeadlinesUseCase,
) : ViewModel() {
    val uiState = getTopHeadlinesUseCase().map {
        it.map(ArticleEntity::asExternalModel)
    }.cachedIn(viewModelScope)
}