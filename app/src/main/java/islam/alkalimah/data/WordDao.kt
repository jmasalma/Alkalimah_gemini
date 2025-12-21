package islam.alkalimah.data

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {
    @Query("""
        SELECT simple, uthmani, translation, transliteration, audio_blob, locations
        FROM words
        ORDER BY locations DESC
        LIMIT :limit
    """)
    fun getTopWords(limit: Int): Flow<List<WordWithFrequency>>
}
