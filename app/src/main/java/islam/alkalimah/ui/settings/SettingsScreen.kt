package islam.alkalimah.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import islam.alkalimah.ui.flashcard.FlashcardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    viewModel: FlashcardViewModel = hiltViewModel()
) {
    val currentLimit by viewModel.currentLimit.collectAsState()
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(text = "Difficulty (Word Count Limit)", style = MaterialTheme.typography.titleMedium)
            Text(
                text = "Current: Top $currentLimit most frequent words",
                style = MaterialTheme.typography.bodySmall
            )
            
            Slider(
                value = currentLimit.toFloat(),
                onValueChange = { viewModel.updateLevel(it.toInt()) },
                valueRange = 10f..1000f,
                steps = 99
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { viewModel.resetProgress() },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Reset All Progress")
            }
        }
    }
}