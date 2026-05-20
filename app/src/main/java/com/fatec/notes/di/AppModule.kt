package com.fatec.notes.di

import com.fatec.notes.data.InMemoryNoteRepository
import com.fatec.notes.data.InMemoryQuestionRepository
import com.fatec.notes.data.NoteRepository
import com.fatec.notes.data.QuestionRepository
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module
import com.fatec.notes.viewmodel.NoteViewModel
import com.fatec.notes.viewmodel.QuestionFormViewModel
import org.koin.core.module.dsl.viewModelOf

/**
 * DI = Dependency injection (Injeção de Depedencia)
 */
val appModule = module {
    singleOf(::InMemoryNoteRepository) bind NoteRepository::class

    viewModelOf(::NoteViewModel)

    singleOf(::InMemoryQuestionRepository) bind QuestionRepository::class
    viewModelOf(::QuestionFormViewModel)
}