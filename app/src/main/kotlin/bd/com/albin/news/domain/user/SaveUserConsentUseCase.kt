package bd.com.albin.news.domain.user

import bd.com.albin.news.data.repository.user.UserDataRepository
import javax.inject.Inject

class SaveUserConsentUseCase @Inject constructor(private val repository: UserDataRepository) {
    suspend operator fun invoke(userConsent: Boolean) = repository.saveUserConsent(userConsent)
}