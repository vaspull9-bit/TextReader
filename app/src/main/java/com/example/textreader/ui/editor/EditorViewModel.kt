package com.example.textreader.ui.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.textreader.data.Note
import com.example.textreader.data.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditorViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {

    fun createNote(title: String, content: String, imagePath: String? = null) {
        viewModelScope.launch {
            val note = Note(title = title, content = content, imagePath = imagePath)
            repository.insertNote(note)
        }
    }

    fun updateNote(id: Long, title: String, content: String, imagePath: String? = null) {
        viewModelScope.launch {
            val note = Note(id, title, content, imagePath = imagePath)
            repository.updateNote(note)
        }
    }
}