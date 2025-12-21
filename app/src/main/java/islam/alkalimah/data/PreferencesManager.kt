package islam.alkalimah.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

class PreferencesManager(private val context: Context) {
    private val Context.dataStore by preferencesDataStore("settings")

    val currentCardIndex = context.dataStore.data.map { it[INDEX_KEY] ?: 0 }
    val advancedLevel = context.dataStore.data.map { it[LEVEL_KEY] ?: 10 }

    suspend fun saveProgress(index: Int) {
        context.dataStore.edit { it[INDEX_KEY] = index }
    }

    suspend fun updateLevel(limit: Int) {
        context.dataStore.edit { it[LEVEL_KEY] = limit }
    }

    suspend fun reset() {
        context.dataStore.edit {
            it[INDEX_KEY] = 0
            it[LEVEL_KEY] = 10
        }
    }

    companion object {
        val INDEX_KEY = intPreferencesKey("card_index")
        val LEVEL_KEY = intPreferencesKey("advanced_limit")
    }
}
