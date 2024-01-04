package bd.com.albin.news.data.repository.user

import kotlinx.coroutines.flow.Flow

interface UserDataRepository {

    val consentGranted : Flow<Boolean>

    suspend fun saveUserConsent(consentGranted: Boolean)

}