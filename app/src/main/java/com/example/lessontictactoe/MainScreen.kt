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

@Composable
fun MainScreen(modifier: Modifier = Modifier, dim: Int) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Tic Tac Toe",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        GameBoard(dim = dim)
    }
}

@Composable
fun GameBoard(dim: Int)
{
    val field= remember { mutableStateListOf(*Array(dim*dim) {"_"}) }
    var currentPlayer by remember { mutableStateOf("X") }
    var winner by remember { mutableStateOf<String?>(null) }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        for (row in 0 until dim)
        {
            Row {
                for (col in 0 until dim) {
                    val index = row*dim+col
                    Box(
                        modifier= Modifier.size(80.dp)
                            .padding(4.dp)
                            .border(2.dp,
                                MaterialTheme.colorScheme.primary)
                            .clickable{
                                if(field[index]=="_" && winner==null){
                                    field[index]=currentPlayer
                                    winner = checkWinner(field, dim)
                                    if (winner == null) {
                                        currentPlayer=if(currentPlayer=="X") "0" else "X"
                                    }
                                }
                            }
                        ,
                        contentAlignment = Alignment.Center
                    )
                    {
                        Text(
                            text=field[index],
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                }
            }
        }
        if (winner != null) {
            Text(
                text = "Переможець: $winner",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(top = 16.dp)
            )
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
        MainScreen(dim = 3)
    }
}