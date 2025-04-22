package com.example.lessontictactoe

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun GameSetupScreen(onSelectionComplete: (Int, Int) -> Unit, isDarkTheme: Boolean, onThemeChanged: (Boolean) -> Unit) {
    val sizes = listOf(3, 4, 5)
    var timerDuration by remember { mutableStateOf(10) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
        Text(
            text = "Розмір поля",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )
        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            sizes.forEach { size ->
                OutlinedButton(
                    onClick = { onSelectionComplete(size, timerDuration) },
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Text("${size}x${size}")
                }
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Час на хід",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedButton(
                onClick = { if (timerDuration > 10) timerDuration -= 10 },
                enabled = timerDuration > 10,
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Text("-10 сек")
            }
            Text(
                text = "$timerDuration сек",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            OutlinedButton(
                onClick = { timerDuration += 10 },
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Text("+10 сек")
            }
        }

        }
      }
        Row(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp)
        ) {
            OutlinedButton(
                onClick = { onThemeChanged(!isDarkTheme) },
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Text(if (isDarkTheme) "Світла тема" else "Темна тема")
            }
        }
    }
}