package bd.com.albin.news.domain.user

import bd.com.albin.news.data.repository.user.UserDataRepository
import javax.inject.Inject

class GetUserDataUseCase @Inject constructor(private val repository: UserDataRepository) {
    operator fun invoke() = repository.consentGranted
}