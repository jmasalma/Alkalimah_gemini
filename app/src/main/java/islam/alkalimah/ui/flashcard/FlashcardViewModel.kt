package islam.alkalimah.ui.flashcard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import islam.alkalimah.data.PreferencesManager
import islam.alkalimah.data.WordDao
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class FlashcardViewModel @Inject constructor(
    private val dao: WordDao,
    private val prefs: PreferencesManager
) : ViewModel() {

    val currentLimit = prefs.advancedLevel.stateIn(viewModelScope, SharingStarted.Eagerly, 50)
    val currentIndex = prefs.currentCardIndex.stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    val words = currentLimit.flatMapLatest { limit ->
        dao.getTopWords(limit)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun nextCard() {
        viewModelScope.launch {
            if (currentIndex.value < words.value.size - 1) {
                prefs.saveProgress(currentIndex.value + 1)
            }
        }
    }

    fun updateLevel(limit: Int) {
        viewModelScope.launch {
            prefs.updateLevel(limit)
        }
    }

    fun resetProgress() {
        viewModelScope.launch {
            prefs.reset()
        }
    }
}
