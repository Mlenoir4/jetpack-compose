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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListScreen(navController: NavController, projectId: Int) {
    val context = LocalContext.current
    var text by remember { mutableStateOf("") }
    val projectsList = loadAllProjectDataByProjectId(LocalContext.current, projectId)
    val taskList = remember { mutableStateListOf<TasksList>() }
    for (project in projectsList!!.projects) {
        taskList.add(loadTaskDataByProjectId(LocalContext.current, project.id)!!)
    }
    val currentProject = projectsList.projects[0];

    var showDialog by remember { mutableStateOf(false) }
    var newEtat by remember { mutableStateOf("") }
    var newTitle by remember { mutableStateOf("") }
    var newDescription by remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Button(
                onClick = { showDialog = true },
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

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text(text = "Ajouter une tâche") },
                    text = {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                TextField(
                                    value = newEtat,
                                    onValueChange = { newEtat = it },
                                    label = { Text("Etat") }
                                )
                            }
                            TextField(
                                value = newTitle,
                                onValueChange = { newTitle = it },
                                label = { Text("Titre") }
                            )
                            TextField(
                                value = newDescription,
                                onValueChange = { newDescription = it },
                                label = { Text("Description") }
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                val newTask = Tasks(
                                    id = getLastId(context, "tasks.json") + 1,
                                    project_id = projectId,
                                    etat = newEtat,
                                    title = newTitle,
                                    desciption = newDescription
                                )
                                taskList.add(TasksList(listOf(newTask)))
                                saveTasksToFile(context, taskList)
                                showDialog = false
                                newEtat = ""
                                newTitle = ""
                                newDescription = ""
                            }
                        ) {
                            Text("Ajouter")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { showDialog = false }) {
                            Text("Annuler")
                        }
                    }
                )
            }

            LazyColumn(modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)) {
                item {
                    Text(
                        text = currentProject.title,
                        style = androidx.compose.material.MaterialTheme.typography.h4.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF6200EE)
                        ),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Text(
                        text = currentProject.descritpion,
                        modifier = Modifier.padding(bottom = 5.dp)
                    )
                    Text(
                        text =  "Date de début: ${currentProject.date_start}",
                        modifier = Modifier.padding(bottom = 5.dp)
                    )
                    Text(
                        text =  "Date de fin: ${currentProject.date_end}",
                        modifier = Modifier.padding(bottom = 5.dp)
                    )
                }
            }

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                item {
                    Text(
                        text = "Les tâches du projet",
                        style = androidx.compose.material.MaterialTheme.typography.h6.copy(
                            fontWeight = FontWeight.Bold,
                        ),
                        modifier = Modifier.padding(18.dp)
                    )
                }
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
                text = item.etat.toString(),
                modifier = Modifier.padding(start = 16.dp),
                style = TextStyle(fontWeight = FontWeight.Bold, color = Color(0xFF6200EE)),
            )
            Text(
                text = item.title,
                modifier = Modifier.padding(start = 16.dp),
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = item.desciption,
                modifier = Modifier.padding(start = 16.dp),
            )
        }
        IconButton(onClick = onDelete) {
            Icon(Icons.Filled.Delete, contentDescription = "Delete")
        }
    }
}
