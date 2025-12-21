package islam.alkalimah.data

data class WordWithFrequency(
    val simple: String,
    val uthmani: String?,
    val translation: String?,
    val transliteration: String?,
    val audio_blob: ByteArray?,
    val locations: Int
)
