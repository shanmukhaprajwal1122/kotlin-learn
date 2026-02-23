package com.example.a1.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.a1.data.AppDatabase
import com.example.a1.data.Note
import com.example.a1.data.NoteDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SharedViewModel(application: Application) : AndroidViewModel(application) {

    // Database and DAO
    private val noteDao: NoteDao

    // LiveData from database (auto-updates)
    val notesList: LiveData<List<Note>>

    init {
        // Get database instance
        val database = AppDatabase.getDatabase(application)
        noteDao = database.noteDao()

        // Get LiveData from DAO
        notesList = noteDao.getAllNotes()
    }

    // ═══════════════════════════════════════════════
    // INSERT OPERATIONS
    // ═══════════════════════════════════════════════

    fun addNote(title: String, content: String) {
        val note = Note(
            title = title.ifEmpty { "Untitled" },
            content = content
        )

        // Launch coroutine for database operation
        viewModelScope.launch(Dispatchers.IO) {
            noteDao.insert(note)
        }
    }

    // ═══════════════════════════════════════════════
    // UPDATE OPERATIONS
    // ═══════════════════════════════════════════════

    fun updateNote(noteId: String, title: String, content: String) {
        viewModelScope.launch(Dispatchers.IO) {
            // Get existing note
            val existingNote = noteDao.getNoteById(noteId)

            existingNote?.let { note ->
                // Create updated note
                val updatedNote = note.copy(
                    title = title.ifEmpty { "Untitled" },
                    content = content,
                    timestamp = System.currentTimeMillis()
                )

                // Update in database
                noteDao.update(updatedNote)
            }
        }
    }

    // ═══════════════════════════════════════════════
    // DELETE OPERATIONS
    // ═══════════════════════════════════════════════

    fun deleteNote(noteId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            noteDao.deleteById(noteId)
        }
    }

    fun clearAllNotes() {
        viewModelScope.launch(Dispatchers.IO) {
            noteDao.deleteAll()
        }
    }

    // ═══════════════════════════════════════════════
    // QUERY OPERATIONS
    // ═══════════════════════════════════════════════

    fun getNoteById(noteId: String, callback: (Note?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val note = noteDao.getNoteById(noteId)
            // Switch back to main thread for callback
            launch(Dispatchers.Main) {
                callback(note)
            }
        }
    }
}

