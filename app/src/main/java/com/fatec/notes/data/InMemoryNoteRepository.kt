package com.fatec.notes.data

import com.fatec.notes.model.Note

class InMemoryNoteRepository : NoteRepository {
    private val notes = mutableListOf<Note>()

    override fun getAll(): List<Note> = notes.toList()

    override fun add(note: Note) {
        notes.add(note)
    }

    override fun remove(noteId: Long) {
        notes.removeAll { it.id == noteId }
    }
}