package com.fatec.notes.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fatec.notes.viewmodel.QuestionFormViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun QuestionFormScreen(
    onBack: () -> Unit,
    viewModel: QuestionFormViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    MaterialTheme {
        Surface (modifier = Modifier.fillMaxSize()){
            Column (
                modifier = Modifier.fillMaxSize().statusBarsPadding().padding(16.dp)
            ){
                // Cabeçalho
                Row (verticalAlignment = Alignment.CenterVertically){
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Voltar")
                    }
                    Text(
                        "Cadastrar Questão",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                // Conteudo com scroll


            }
        }
    }
}