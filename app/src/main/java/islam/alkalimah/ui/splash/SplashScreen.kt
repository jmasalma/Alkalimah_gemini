package islam.alkalimah.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import islam.alkalimah.R

@Composable
fun SplashScreen(onNavigateToFlashcard: () -> Unit, onNavigateToSettings: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Image(painter = painterResource(id = R.mipmap.ic_launcher), contentDescription = "App icon")
            Spacer(modifier = Modifier.height(16.dp))
            Text("Alkalimah", fontSize = 48.sp)
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = onNavigateToFlashcard,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Start", fontSize = 24.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onNavigateToSettings,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Settings", fontSize = 24.sp)
            }
        }
    }
}
