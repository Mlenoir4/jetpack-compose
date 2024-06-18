package com.example.myapplication

import android.util.Log
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

data class TodoItem(val text: String, var isChecked: Boolean = false)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListScreen(navController: NavController, userId: Int) {
    var text by remember { mutableStateOf("") }
    //recuperer l'id passer en param de la page
    val projectsList = loadAllProjectDataByUserId(LocalContext.current, userId)
    val taskList = mutableListOf<TasksList>()
    for (project in projectsList!!.projects) {
        taskList.add(loadTaskDataByProjectId(LocalContext.current, project.id)!!)
    }

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
                    if (text.isNotBlank()) {
                        taskList.add(TasksList(listOf(Tasks(0, 0, "todo", text, ""))))
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
                items(taskList) { task ->
                    TodoItem(
                        item = task.tasks[0],
                        onCheckChange = { isChecked ->
                        },
                        onDelete = {
                            taskList.remove(task)
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun TodoItem(item: Tasks, onCheckChange: (Boolean) -> Unit, onDelete: () -> Unit) {
    val backgroundColor = Color.White
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
        Column {
            Text(
                text = item.id.toString(),
                modifier = Modifier.padding(start = 8.dp),
                textDecoration = TextDecoration.Underline
            )
            Text(
                text = item.etat,
                modifier = Modifier.padding(start = 8.dp),
                textDecoration = TextDecoration.Underline
            )
            Text(
                //mettre en gras
                text = item.title,
                modifier = Modifier.padding(start = 16.dp),
                textDecoration = TextDecoration.Underline,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = item.desciption,
                modifier = Modifier.padding(start = 8.dp),
                textDecoration = TextDecoration.Underline
            )
        }
        IconButton(onClick = onDelete) {
            Icon(Icons.Filled.Delete, contentDescription = "Delete")
        }
    }
}