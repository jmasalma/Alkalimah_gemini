package islam.alkalimah.ui.flashcard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import islam.alkalimah.utils.AudioPlayer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashcardScreen(viewModel: FlashcardViewModel, audioPlayer: AudioPlayer, onNavigateToSettings: () -> Unit, onNavigateToCompletion: () -> Unit) {
    val words by viewModel.words.collectAsState()
    val index by viewModel.currentIndex.collectAsState()
    val showTransliteration by viewModel.showTransliteration.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Alkalimah") },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (words.isNotEmpty() && index < words.size) {
            val word = words[index]

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceAround
                    ) {
                        Text(
                            text = word.uthmani ?: "",
                            fontSize = 72.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = word.translation ?: "",
                            fontSize = 48.sp,
                            textAlign = TextAlign.Center
                        )
                        if (showTransliteration) {
                            Text(
                                text = word.transliteration ?: "",
                                fontSize = 24.sp,
                                fontStyle = FontStyle.Italic
                            )
                        }

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                        ) {
                            IconButton(
                                onClick = { audioPlayer.playAudio(word.audio_blob) },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    Icons.Default.PlayArrow,
                                    contentDescription = "Play",
                                    modifier = Modifier.size(64.dp)
                                )
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Button(
                        onClick = { viewModel.previousCard() },
                        modifier = Modifier.weight(0.33f)
                    ) {
                        Text("Prev", fontSize = 24.sp)
                    }
                    Button(
                        onClick = {
                            if (index == words.size - 1) {
                                onNavigateToCompletion()
                            } else {
                                viewModel.nextCard()
                            }
                        },
                        modifier = Modifier
                            .weight(0.67f)
                            .padding(start = 8.dp)
                    ) {
                        Text("Next", fontSize = 24.sp)
                    }
                }
            }
        }
    }
}
