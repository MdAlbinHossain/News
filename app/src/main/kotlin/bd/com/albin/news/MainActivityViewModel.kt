package bd.com.albin.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bd.com.albin.news.domain.user.GetUserDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(getUserDataUseCase: GetUserDataUseCase) :
    ViewModel() {
    val uiState: StateFlow<MainActivityUiState> = getUserDataUseCase.invoke().map {
        MainActivityUiState.Success(it)
    }.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = MainActivityUiState.Loading
    )
}

sealed interface MainActivityUiState {
    data object Loading : MainActivityUiState
    data class Success(val userConsentGranted: Boolean) : MainActivityUiState
}