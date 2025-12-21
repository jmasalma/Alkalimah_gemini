package islam.alkalimah.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import islam.alkalimah.ui.completion.CompletionScreen
import islam.alkalimah.ui.flashcard.FlashcardScreen
import islam.alkalimah.ui.settings.SettingsScreen
import islam.alkalimah.ui.splash.SplashScreen
import islam.alkalimah.utils.AudioPlayer
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
                NavHost(
                    navController = navController,
                    startDestination = "splash",
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable("splash") {
                        SplashScreen {
                            navController.navigate("flashcards") {
                                popUpTo("splash") { inclusive = true }
                            }
                        }
                    }
                    composable("flashcards") {
                        FlashcardScreen(
                            viewModel = hiltViewModel(),
                            onNavigateToSettings = { navController.navigate("settings") },
                            onNavigateToCompletion = { navController.navigate("completion") },
                            audioPlayer = audioPlayer
                        )
                    }
                    composable("settings") {
                        SettingsScreen(
                            onBack = { navController.popBackStack() }
                        )
                    }
                    composable("completion") {
                        CompletionScreen(
                            onNavigateToSettings = {
                                navController.navigate("settings") {
                                    popUpTo("flashcards") { inclusive = true }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}