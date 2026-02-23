package com.example.a1.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDao {

    // ═══════════════════════════════════════════════
    // INSERT OPERATIONS
    // ═══════════════════════════════════════════════

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(notes: List<Note>)


    // ═══════════════════════════════════════════════
    // UPDATE OPERATIONS
    // ═══════════════════════════════════════════════

    @Update
    suspend fun update(note: Note)


    // ═══════════════════════════════════════════════
    // DELETE OPERATIONS
    // ═══════════════════════════════════════════════

    @Delete
    suspend fun delete(note: Note)

    @Query("DELETE FROM notes WHERE id = :noteId")
    suspend fun deleteById(noteId: String)

    @Query("DELETE FROM notes")
    suspend fun deleteAll()


    // ═══════════════════════════════════════════════
    // QUERY OPERATIONS
    // ═══════════════════════════════════════════════

    // Get all notes (LiveData - auto-updates UI)
    @Query("SELECT * FROM notes ORDER BY timestamp DESC")
    fun getAllNotes(): LiveData<List<Note>>

    // Get note by ID
    @Query("SELECT * FROM notes WHERE id = :noteId")
    suspend fun getNoteById(noteId: String): Note?

    // Search notes by title or content
    @Query("SELECT * FROM notes WHERE title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%' ORDER BY timestamp DESC")
    fun searchNotes(query: String): LiveData<List<Note>>

    // Get notes count
    @Query("SELECT COUNT(*) FROM notes")
    suspend fun getNotesCount(): Int
}

