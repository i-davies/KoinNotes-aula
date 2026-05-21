package com.fatec.notes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.fatec.notes.ui.screens.HomeScreen
import com.fatec.notes.ui.screens.NoteScreen
import com.fatec.notes.ui.screens.QuestionFormScreen

private enum class Screen { HOME, NOTES, QUESTIONS }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            var currentScreen by remember { mutableStateOf(Screen.HOME) }

            when (currentScreen) {
                Screen.HOME -> HomeScreen(
                    onNavigateToNotes = { currentScreen = Screen.NOTES },
                    onNavigateToQuestions = { currentScreen = Screen.QUESTIONS }
                )
                Screen.NOTES -> NoteScreen(
                    onBack = { currentScreen = Screen.HOME }
                )
                Screen.QUESTIONS -> QuestionFormScreen(
                    onBack = { currentScreen = Screen.HOME }
                )
            }
        }
    }
}