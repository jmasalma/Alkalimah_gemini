package islam.alkalimah.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "words")
data class WordEntity(
    @PrimaryKey val id: Int?,
    val location: String?,
    val uthmani: String?,
    val simple: String?,
    val translation: String?,
    val transliteration: String?,
    val locations: Int?,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB) val audio_blob: ByteArray?
)
