package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme

data class TodoItem(val text: String, var isChecked: Boolean = false)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListScreen() {
    var todoItems by remember {
        mutableStateOf(listOf(
            TodoItem("Se lever"),
            TodoItem("Manger"),
            TodoItem("Jouer"),
            TodoItem("Dormir")
        ))
    }
    var text by remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            TextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Ajouter une tâche") },
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = {
                    if (text.isNotEmpty()) {
                        todoItems = todoItems + TodoItem(text)
                        text = ""
                    }
                },
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 8.dp),
                shape = MaterialTheme.shapes.small.copy(CornerSize(50)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6200EE),
                )
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(items = todoItems, key = { it.text }) { item ->
                    TodoItem(
                        item = item,
                        onCheckChange = { isChecked ->
                            todoItems = todoItems.map { todo ->
                                if (todo.text == item.text) todo.copy(isChecked = isChecked) else todo
                            }
                        },
                        onDelete = {
                            todoItems = todoItems.filter { it.text != item.text }
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun TodoItem(item: TodoItem, onCheckChange: (Boolean) -> Unit, onDelete: () -> Unit) {
    val backgroundColor = if (item.isChecked) Color(0xFFE0E0E0) else Color.White
    val borderColor = Color(0xFF6200EE)
    val borderWidth = 1.dp
    val borderRadius = 16.dp

    Row(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(borderRadius))
            .background(backgroundColor)
            .border(borderWidth, borderColor, RoundedCornerShape(borderRadius))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Checkbox(
            checked = item.isChecked,
            onCheckedChange = onCheckChange
        )
        Text(
            text = item.text,
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
            textDecoration = if (item.isChecked) TextDecoration.LineThrough else null
        )
        IconButton(onClick = onDelete) {
            Icon(Icons.Filled.Delete, contentDescription = "Delete")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TodoListScreenPreview() {
    MyApplicationTheme {
        TodoListScreen()
    }
}