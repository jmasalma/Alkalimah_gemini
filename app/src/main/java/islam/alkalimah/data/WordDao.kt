package islam.alkalimah.data

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {
    @Query("""
        SELECT simple, uthmani, translation, transliteration, audio_blob, COUNT(location) as locations
        FROM words
        WHERE length(simple) > 2
        GROUP BY simple
        ORDER BY locations DESC
        LIMIT :limit
    """)
    fun getTopWords(limit: Int): Flow<List<WordWithFrequency>>
}
