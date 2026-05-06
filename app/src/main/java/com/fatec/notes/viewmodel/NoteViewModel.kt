package com.fatec.notes.viewmodel

import androidx.lifecycle.ViewModel
import com.fatec.notes.data.NoteRepository
import com.fatec.notes.model.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * UI State Centralizado
 */
data class NoteUiState (
    val notes: List<Note> = emptyList(),
    val inputTitle: String = "",
    val inputContent: String = ""
) {
    val totalNotes: Int get() = notes.size
    val isEmpty: Boolean get() = notes.isEmpty()
}

class NoteViewModel(
    private val repository: NoteRepository  // Koin injeta automaticamente
) : ViewModel() {

    private val _uiState = MutableStateFlow(NoteUiState())
    val uiState: StateFlow<NoteUiState> = _uiState.asStateFlow()

    fun onTitleChange(newTitle: String) {
        _uiState.value = _uiState.value.copy(inputTitle = newTitle)
    }

    fun onContentChange(newContent: String) {
        _uiState.value = _uiState.value.copy(inputContent = newContent)
    }

    fun addNote() {
        val current = _uiState.value
        if(current.inputTitle.isBlank()) return

        val note = Note(
            title = current.inputTitle.trim(),
            content = current.inputContent.trim()
        )
        repository.add(note) // Delega ao Repository

        _uiState.value = current.copy(
            notes = repository.getAll(), // Busca do Repository
            inputTitle = "",
            inputContent = ""
        )
    }

    fun removeNote(noteId: Long) {
        repository.remove(noteId) // Delega ao Repository
        _uiState.value = _uiState.value.copy(notes = repository.getAll())
    }

}