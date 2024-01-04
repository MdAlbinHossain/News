package bd.com.albin.news.ui.screens.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bd.com.albin.news.domain.user.SaveUserConsentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(private val saveUserConsentUseCase: SaveUserConsentUseCase) :
    ViewModel() {
    fun saveUserConsent() {
        viewModelScope.launch {
            saveUserConsentUseCase.invoke(true)
        }
    }
}