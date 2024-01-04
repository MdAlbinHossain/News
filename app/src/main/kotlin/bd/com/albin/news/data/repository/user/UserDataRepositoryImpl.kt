package bd.com.albin.news.data.repository.user

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class UserDataRepositoryImpl @Inject constructor(private val dataStore: DataStore<Preferences>) :
    UserDataRepository {
    private companion object {
        val CONSENT_GRANTED = booleanPreferencesKey("consent_granted")
        const val TAG = "UserPreferencesRepo"
    }

    override val consentGranted: Flow<Boolean> = dataStore.data.catch {
        if (it is IOException) {
            Log.e(TAG, "Error reading user preferences.", it)
        } else {
            throw it
        }
    }.map { preferences ->
        preferences[CONSENT_GRANTED] ?: false
    }

    override suspend fun saveUserConsent(consentGranted: Boolean) {
        dataStore.edit { preferences ->
            preferences[CONSENT_GRANTED] = consentGranted
        }
    }
}