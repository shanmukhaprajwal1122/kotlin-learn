package com.example.a1

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {

    private val _notesList = MutableLiveData<List<Note>>(emptyList())
    val notesList: LiveData<List<Note>> = _notesList

    // Add new note
    fun addNote(title: String, content: String) {
        val currentList = _notesList.value ?: emptyList()
        val newNote = Note(
            title = title.ifEmpty { "Untitled" },
            content = content
        )
        val newList = listOf(newNote) + currentList  // New note at top
        _notesList.value = newList
    }

    // Update existing note
    fun updateNote(noteId: String, title: String, content: String) {
        val currentList = _notesList.value ?: emptyList()
        val newList = currentList.map { note ->
            if (note.id == noteId) {
                note.copy(
                    title = title.ifEmpty { "Untitled" },
                    content = content,
                    timestamp = System.currentTimeMillis()
                )
            } else {
                note
            }
        }
        _notesList.value = newList
    }

    // Delete note
    fun deleteNote(noteId: String) {
        val currentList = _notesList.value ?: emptyList()
        val newList = currentList.filter { it.id != noteId }
        _notesList.value = newList
    }

    // Get note by ID
    fun getNoteById(noteId: String): Note? {
        return _notesList.value?.find { it.id == noteId }
    }

    // Clear all notes
    fun clearAllNotes() {
        _notesList.value = emptyList()
    }
}