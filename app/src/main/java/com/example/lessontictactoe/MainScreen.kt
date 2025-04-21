package com.example.lessontictactoe

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.LayoutModifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lessontictactoe.ui.theme.LessonTicTacToeTheme
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.delay

@Composable
fun MainScreen(modifier: Modifier = Modifier, dim: Int, timerDuration: Int) {
    var scoreX by remember { mutableStateOf(0) }
    var scoreO by remember { mutableStateOf(0) }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Хрестики-Нулики",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textAlign = TextAlign.Center
        )
        Row(modifier = Modifier.padding(bottom = 16.dp)) {
            Text(text = "Рахунок X: $scoreX", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(end = 16.dp))
            Text(text = "Рахунок O: $scoreO", style = MaterialTheme.typography.bodyLarge)
        }
        Spacer(modifier = Modifier.height(8.dp))
        GameBoard(dim = dim, timerDuration = timerDuration, onGameEnd = { winner ->
            if (winner == "X") scoreX++
            if (winner == "0") scoreO++
        })
    }
}

@Composable
fun GameBoard(dim: Int, timerDuration: Int, onGameEnd: (String?) -> Unit)
{
    val field = remember { mutableStateListOf(*Array(dim * dim) { "_" }) }
    var currentPlayer by remember { mutableStateOf("X") }
    var winner by remember { mutableStateOf<String?>(null) }
    var isDraw by remember { mutableStateOf(false) }
    var remainingTime by remember { mutableStateOf(timerDuration) }
    var timerKey by remember { mutableStateOf(0) }

    LaunchedEffect(currentPlayer, timerKey, winner, isDraw) {
        if (winner == null && !isDraw) {
            remainingTime = timerDuration
            while (remainingTime > 0) {
                delay(1000)
                remainingTime--
            }
            if (remainingTime == 0 && winner == null && !isDraw) {
                currentPlayer = if (currentPlayer == "X") "0" else "X"
                timerKey++
            }
        }
    }

    fun resetGame() {
        field.clear()
        field.addAll(List(dim * dim) { "_" })
        winner = null
        isDraw = false
        currentPlayer = "X"
        timerKey++ 
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Хід гравця: $currentPlayer",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Час: $remainingTime сек",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        for (row in 0 until dim)
        {
            Row {
                for (col in 0 until dim) {
                    val index = row * dim + col
                    Box(
                        modifier = Modifier.size(80.dp)
                            .padding(4.dp)
                            .border(
                                2.dp,
                                MaterialTheme.colorScheme.primary
                            )
                            .clickable {
                                if (field[index] == "_" && winner == null && !isDraw) {
                                    field[index] = currentPlayer
                                    winner = checkWinner(field, dim)
                                    if (winner != null) {
                                        onGameEnd(winner)
                                    } else if (field.none { it == "_" }) {
                                        isDraw = true
                                        onGameEnd(null)
                                    } else {
                                        currentPlayer = if (currentPlayer == "X") "0" else "X"
                                        timerKey++
                                    }
                                }
                            },
                        contentAlignment = Alignment.Center
                    )
                    {
                        Text(
                            text = field[index],
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (winner != null) {
            Text(
                text = "Переможець: $winner",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(top = 16.dp)
            )
        } else if (isDraw) {
            Text(
                text = "Нічия!",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        if (winner != null || isDraw) {
            OutlinedButton(onClick = { resetGame() }, modifier = Modifier.padding(top = 8.dp)) {
                Text("Грати ще")
            }
        }
    }
}

fun checkWinner(field: List<String>, dim: Int): String? {
    for (row in 0 until dim) {
        val first = field[row * dim]
        if (first != "_" && (1 until dim).all { field[row * dim + it] == first }) {
            return first
        }
    }
    for (col in 0 until dim) {
        val first = field[col]
        if (first != "_" && (1 until dim).all { field[it * dim + col] == first }) {
            return first
        }
    }
    val firstDiag = field[0]
    if (firstDiag != "_" && (1 until dim).all { field[it * dim + it] == firstDiag }) {
        return firstDiag
    }
    val firstAntiDiag = field[dim - 1]
    if (firstAntiDiag != "_" && (1 until dim).all { field[it * dim + (dim - 1 - it)] == firstAntiDiag }) {
        return firstAntiDiag
    }
    return null
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview()
{
    LessonTicTacToeTheme {
        MainScreen(dim = 3, timerDuration = 10)
    }
}