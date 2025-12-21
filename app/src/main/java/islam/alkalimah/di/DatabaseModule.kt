package islam.alkalimah.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import islam.alkalimah.data.AppDatabase
import islam.alkalimah.data.PreferencesManager
import islam.alkalimah.data.WordDao
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "alkalimah_words.db"
        )
        .createFromAsset("databases/alkalimah_words.db") // Copies your uploaded DB
        .fallbackToDestructiveMigration()
        .build()
    }

    @Provides
    fun provideWordDao(db: AppDatabase): WordDao = db.wordDao()

    @Provides
    @Singleton
    fun providePreferencesManager(@ApplicationContext context: Context): PreferencesManager {
        return PreferencesManager(context)
    }
}