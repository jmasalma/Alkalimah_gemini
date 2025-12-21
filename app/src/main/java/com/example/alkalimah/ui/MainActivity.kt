package com.example.alkalimah.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Scaffold
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.alkalimah.ui.flashcard.FlashcardScreen
import com.example.alkalimah.ui.settings.SettingsScreen
import com.example.alkalimah.utils.AudioPlayer
import dagger.hilt.android.AndroidEntryPoint
import androidx.hilt.navigation.compose.hiltViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val audioPlayer = AudioPlayer(this)

        setContent {
            val navController = rememberNavController()
            
            Scaffold { innerPadding ->
                NavHost(navController, startDestination = "flashcards") {
                    composable("flashcards") {
                        FlashcardScreen(
                            viewModel = hiltViewModel(),
                            onNavigateToSettings = { navController.navigate("settings") },
                            audioPlayer = audioPlayer
                        )
                    }
                    composable("settings") {
                        SettingsScreen(
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}