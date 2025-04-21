package com.example.lessontictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.lessontictactoe.ui.theme.LessonTicTacToeTheme
import androidx.compose.runtime.*
import com.example.lessontictactoe.GameSetupScreen
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var isDarkTheme by remember { mutableStateOf(false) }
            LessonTicTacToeTheme(darkTheme = isDarkTheme) {
                var selectedSize by remember { mutableStateOf<Int?>(null) }
                var selectedTimerDuration by remember { mutableStateOf<Int?>(null) }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    if (selectedSize == null || selectedTimerDuration == null) {
                        GameSetupScreen(
                            onSelectionComplete = { size, duration ->
                                selectedSize = size
                                selectedTimerDuration = duration
                            },
                            isDarkTheme = isDarkTheme,
                            onThemeChanged = { isDarkTheme = it }
                        )
                    } else {
                        MainScreen(
                            modifier = Modifier.padding(innerPadding),
                            dim = selectedSize!!,
                            timerDuration = selectedTimerDuration!!,
                            onNewGameRequested = {
                                selectedSize = null
                                selectedTimerDuration = null
                            }
                        )
                    }
                }
            }
        }
    }
}


