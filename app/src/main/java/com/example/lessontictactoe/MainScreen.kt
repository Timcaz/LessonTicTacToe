package com.example.lessontictactoe

import android.media.MediaPlayer
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.lessontictactoe.ui.theme.LessonTicTacToeTheme
import kotlinx.coroutines.delay
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun MainScreen(modifier: Modifier = Modifier, dim: Int, timerDuration: Int, onNewGameRequested: () -> Unit) {
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
        }, onNewGameRequested = onNewGameRequested)
    }
}

@Composable
fun GameBoard(dim: Int, timerDuration: Int, onGameEnd: (String?) -> Unit, onNewGameRequested: () -> Unit) {
    val field = remember { mutableStateListOf(*Array(dim * dim) { "_" }) }
    var currentPlayer by remember { mutableStateOf("X") }
    var winner by remember { mutableStateOf<String?>(null) }
    var isDraw by remember { mutableStateOf(false) }
    var remainingTime by remember { mutableStateOf(timerDuration) }
    var timerKey by remember { mutableStateOf(0) }
    var lastMoveIndex by remember { mutableStateOf(-1) }
    val context = LocalContext.current
    val movePlayer = remember { MediaPlayer.create(context, R.raw.move_sound) }
    var winLine by remember { mutableStateOf<List<Int>?>(null) }

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
        lastMoveIndex = -1
        winLine = null
    }

    fun findWinLine(): List<Int>? {
        for (row in 0 until dim) {
            if (field[row * dim] != "_" && (1 until dim).all { field[row * dim + it] == field[row * dim] })
                return List(dim) { row * dim + it }
        }
        for (col in 0 until dim) {
            if (field[col] != "_" && (1 until dim).all { field[it * dim + col] == field[col] })
                return List(dim) { it * dim + col }
        }
        if (field[0] != "_" && (1 until dim).all { field[it * dim + it] == field[0] })
            return List(dim) { it * dim + it }
        if (field[dim - 1] != "_" && (1 until dim).all { field[it * dim + (dim - 1 - it)] == field[dim - 1] })
            return List(dim) { it * dim + (dim - 1 - it) }
        return null
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp)
    ) {
        Text(
            text = "Хід гравця: $currentPlayer",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp),
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Час: $remainingTime сек",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 16.dp),
            color = MaterialTheme.colorScheme.secondary
        )
        val cellSize = if (dim <= 3) 96.dp else if (dim == 4) 72.dp else 56.dp
        for (row in 0 until dim) {
            Row(horizontalArrangement = Arrangement.Center) {
                for (col in 0 until dim) {
                    val index = row * dim + col
                    val isLastMove = index == lastMoveIndex
                    val isWinCell = winLine?.contains(index) == true
                    val scale by animateFloatAsState(
                        targetValue = if (isLastMove) 1.15f else 1f,
                        animationSpec = tween(durationMillis = 350)
                    )
                    val bgColor by animateColorAsState(
                        targetValue = when {
                            isWinCell -> Color(0xFFB9F6CA)
                            isLastMove -> Color(0xFFE3F2FD)
                            else -> Color.White
                        },
                        animationSpec = tween(durationMillis = 350)
                    )
                    Box(
                        modifier = Modifier
                            .size(cellSize)
                            .padding(3.dp)
                            .border(
                                width = 2.dp,
                                color = if (isWinCell) Color(0xFF00C853) else MaterialTheme.colorScheme.primary,
                                shape = MaterialTheme.shapes.medium
                            )
                            .scale(scale)
                            .clickable(enabled = field[index] == "_" && winner == null && !isDraw) {
                                if (field[index] == "_" && winner == null && !isDraw) {
                                    field[index] = currentPlayer
                                    lastMoveIndex = index
                                    try { movePlayer.start() } catch (_: Exception) {}
                                    winner = checkWinner(field, dim)
                                    winLine = findWinLine()
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
                    ) {
                        if (field[index] == "X") {
                            Image(painter = painterResource(id = R.drawable.ic_cross), contentDescription = "X", modifier = Modifier.size(cellSize * 0.7f))
                        } else if (field[index] == "0") {
                            Image(painter = painterResource(id = R.drawable.ic_nought), contentDescription = "0", modifier = Modifier.size(cellSize * 0.7f))
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        if (winner != null) {
            Text(
                text = "Переможець: $winner",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFF00C853),
                modifier = Modifier.padding(top = 16.dp)
            )
        } else if (isDraw) {
            Text(
                text = "Нічия!",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
        if (winner != null || isDraw) {
            Row(modifier = Modifier.padding(top = 8.dp)) {
                OutlinedButton(
                    onClick = { resetGame() },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text("Скинути раунд", style = MaterialTheme.typography.bodyLarge)
                }
                OutlinedButton(onClick = onNewGameRequested) {
                    Text("Нова гра", style = MaterialTheme.typography.bodyLarge)
                }
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
        MainScreen(dim = 3, timerDuration = 10, onNewGameRequested = {})
    }
}