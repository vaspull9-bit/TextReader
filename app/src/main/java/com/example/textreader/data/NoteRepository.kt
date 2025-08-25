package com.example.textreader.data

import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getAllNotes(): Flow<List<Note>>
    suspend fun getNoteById(id: Long): Note?
    suspend fun insertNote(note: Note): Long
    suspend fun updateNote(note: Note)
    suspend fun deleteNote(note: Note)
}

class NoteRepositoryImpl(private val noteDao: NoteDao) : NoteRepository {
    override fun getAllNotes(): Flow<List<Note>> = noteDao.getAllNotes()
    override suspend fun getNoteById(id: Long): Note? = noteDao.getNoteById(id)
    override suspend fun insertNote(note: Note): Long = noteDao.insert(note)
    override suspend fun updateNote(note: Note) = noteDao.update(note)
    override suspend fun deleteNote(note: Note) = noteDao.delete(note)
}