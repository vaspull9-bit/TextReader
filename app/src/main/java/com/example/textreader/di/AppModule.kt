package com.example.textreader.di

import android.content.Context
import androidx.room.Room
import com.example.textreader.data.NoteDatabase
import com.example.textreader.data.NoteRepository
import com.example.textreader.data.NoteRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(@ApplicationContext context: Context): NoteDatabase {
        return Room.databaseBuilder(
            context,
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteDao(database: NoteDatabase) = database.noteDao()

    @Provides
    @Singleton
    fun provideNoteRepository(dao: NoteDatabase.NoteDao): NoteRepository {
        return NoteRepositoryImpl(dao)
    }
}