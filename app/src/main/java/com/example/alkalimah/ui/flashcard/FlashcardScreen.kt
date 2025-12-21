package com.example.alkalimah.ui.flashcard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.alkalimah.utils.AudioPlayer

@Composable
fun FlashcardScreen(viewModel: FlashcardViewModel, audioPlayer: AudioPlayer, onNavigateToSettings: () -> Unit) {
    val words by viewModel.words.collectAsState()
    val index by viewModel.currentIndex.collectAsState()

    if (words.isNotEmpty() && index < words.size) {
        val word = words[index]

        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Card(modifier = Modifier.weight(1f).fillMaxWidth()) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(word.uthmani ?: "", fontSize = 40.sp) // Arabic Text
                    Text(word.transliteration ?: "", fontStyle = FontStyle.Italic)
                    Text(word.translation ?: "", fontSize = 24.sp)

                    IconButton(onClick = { audioPlayer.playAudio(word.audio_blob) }) {
                        Icon(Icons.Default.PlayArrow, "Play")
                    }
                }
            }

            Button(onClick = { viewModel.nextCard() }, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.ArrowForward, null)
                Text("Next")
            }
        }
    }
}
