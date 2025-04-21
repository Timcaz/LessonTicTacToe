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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LessonTicTacToeTheme {
                var selectedSize by remember { mutableStateOf<Int?>(null) }
                var selectedTimerDuration by remember { mutableStateOf<Int?>(null) }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    if (selectedSize == null || selectedTimerDuration == null) {
                        GameSetupScreen(onSelectionComplete = { size, duration ->
                            selectedSize = size
                            selectedTimerDuration = duration
                        })
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


