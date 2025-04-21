package com.example.lessontictactoe

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun BoardSizeSelectionScreen(onSizeSelected: (Int) -> Unit) {
    val sizes = listOf(3, 4, 5)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Оберіть розмір поля",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            textAlign = TextAlign.Center
        )
        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            sizes.forEach { size ->
                OutlinedButton(
                    onClick = { onSizeSelected(size) },
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Text("${size}x${size}")
                }
            }
        }
    }
}