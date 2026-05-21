package com.fatec.notes.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fatec.notes.model.Question
import com.fatec.notes.model.QuestionType
import com.fatec.notes.ui.components.OptionField
import com.fatec.notes.ui.components.QuestionTypeSelector
import com.fatec.notes.ui.components.ValidationBanner
import com.fatec.notes.viewmodel.QuestionFormViewModel
import org.koin.compose.viewmodel.koinViewModel

/**
 *  Tela de Cadastro de Questões
 */
@Composable
fun QuestionFormScreen(
    onBack: () -> Unit,
    viewModel: QuestionFormViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(16.dp)
            ) {
                // Cabeçalho
                Row(verticalAlignment = Alignment.CenterVertically) {
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

                // Conteúdo com scroll
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Banner de erros
                    ValidationBanner(errors = state.allErrors)

                    // Snackbar de sucesso
                    AnimatedVisibility(
                        visible = state.showSuccess,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    Icons.Default.CheckCircle, "Sucesso",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    "Questão salva com sucesso!",
                                    modifier = Modifier.weight(1f)
                                )
                                IconButton(onClick = { viewModel.dismissSuccess() }) {
                                    Icon(
                                        Icons.Default.Close, "Fechar",
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Enunciado
                    Text(
                        "Enunciado",
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                    OutlinedTextField(
                        value = state.questionText,
                        onValueChange = { viewModel.onQuestionTextChange(it) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Digite o enunciado da questão...") },
                        minLines = 2,
                        maxLines = 4,
                        shape = RoundedCornerShape(8.dp),
                        isError = state.textError != null,
                        supportingText = state.textError?.let { { Text(it) } }
                    )

                    // Tipo de questão
                    Text(
                        "Tipo de Questão",
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                    QuestionTypeSelector(
                        selected = state.questionType,
                        onSelect = { viewModel.onQuestionTypeChange(it) }
                    )

                    // Alternativas
                    HorizontalDivider()
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Alternativas (${state.options.size})",
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                        TextButton(
                            onClick = { viewModel.addOption() },
                            enabled = state.canAddOption
                        ) {
                            Icon(
                                Icons.Default.Add, "Adicionar",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text("Adicionar")
                        }
                    }

                    state.options.forEachIndexed { index, option ->
                        Row(
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // RadioButton para Múltipla Escolha
                            if (state.questionType == QuestionType.MULTIPLE_CHOICE) {
                                RadioButton(
                                    selected = state.correctOptionIndex == index,
                                    onClick = { viewModel.onCorrectOptionChange(index) },
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }

                            // Número de ordem para Ordenação
                            if (state.questionType == QuestionType.ORDERING) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.secondaryContainer,
                                    modifier = Modifier
                                        .padding(top = 8.dp)
                                        .size(36.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            "${index + 1}",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = MaterialTheme.colorScheme.onSecondaryContainer
                                        )
                                    }
                                }
                            }

                            OptionField(
                                index = index,
                                value = option,
                                error = state.optionsErrors.getOrNull(index),
                                onChange = { viewModel.onOptionChange(index, it) },
                                onRemove = { viewModel.removeOption(index) },
                                canRemove = state.canRemoveOption,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    // Erro de resposta correta
                    AnimatedVisibility(
                        visible = state.correctAnswerError != null,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Text(
                            text = state.correctAnswerError ?: "",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    // Botão Salvar
                    Spacer(Modifier.height(4.dp))
                    Button(
                        onClick = { viewModel.submitQuestion() },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(
                            Icons.Default.Save, "Salvar",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Salvar Questão")
                    }

                    // Questões Salvas
                    if (state.totalSaved > 0) {
                        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                        Text(
                            "${state.totalSaved} questão(ões) cadastrada(s)",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        state.savedQuestions.forEach { question ->
                            SavedQuestionCard(
                                question = question,
                                onDelete = { viewModel.removeQuestion(question.id) }
                            )
                        }
                    }

                    // Espaço final para scroll
                    Spacer(Modifier.height(16.dp))
                }
            }
        }
    }
}

/**
 * Card de questão salva
 */
@Composable
private fun SavedQuestionCard(
    question: Question,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                if (question.type == QuestionType.MULTIPLE_CHOICE)
                    Icons.Default.RadioButtonChecked
                else
                    Icons.Default.FormatListNumbered,
                contentDescription = question.type.label,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(top = 2.dp)
                    .size(20.dp)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    question.text,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    "${question.type.label} • ${question.options.size} alternativas",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    Icons.Default.Delete, "Remover",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
