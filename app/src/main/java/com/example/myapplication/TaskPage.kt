package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskList(navController: NavController, projectId: Int) {
    val context = LocalContext.current
    val projectsList = loadAllProjectDataByProjectId(context, projectId)
    val taskList = remember { mutableStateListOf<Tasks>() }

    val currentProject = projectsList?.projects?.get(0)

    LaunchedEffect(projectId) {
        val tasks = loadTaskDataByProjectId(context, projectId)
        taskList.addAll(tasks)
    }

    var showDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var newEtat by remember { mutableStateOf("") }
    var newTitle by remember { mutableStateOf("") }
    var newDescription by remember { mutableStateOf("") }
    var selectedTask by remember { mutableStateOf<Tasks?>(null) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }

                Button(
                    onClick = { showDialog = true },
                    shape = MaterialTheme.shapes.small.copy(CornerSize(50)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6200EE),
                    )
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Add")
                }
            }

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text(text = "Ajouter une tâche") },
                    text = {
                        Column {
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
                                taskList.add(newTask)
                                saveTasksToFile(context, taskList)
                                showDialog = false
                                newEtat = "En cours"
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

            if (showEditDialog && selectedTask != null) {
                AlertDialog(
                    onDismissRequest = { showEditDialog = false },
                    title = { Text(text = "Modifier la tâche") },
                    text = {
                        Column {
                            TextField(
                                value = selectedTask!!.title,
                                onValueChange = { selectedTask = selectedTask!!.copy(title = it) },
                                label = { Text("Titre") }
                            )
                            TextField(
                                value = selectedTask!!.desciption,
                                onValueChange = { selectedTask = selectedTask!!.copy(desciption = it) },
                                label = { Text("Description") }
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                val index = taskList.indexOfFirst { it.id == selectedTask!!.id }
                                if (index != -1) {
                                    taskList[index] = selectedTask!!
                                    saveTasksToFile(context, taskList)
                                }
                                showEditDialog = false
                            }
                        ) {
                            Text("Modifier")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { showEditDialog = false }) {
                            Text("Annuler")
                        }
                    }
                )
            }

            if (currentProject != null) {
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
                            text = "Date de début: ${currentProject.date_start}",
                            modifier = Modifier.padding(bottom = 5.dp)
                        )
                        Text(
                            text = "Date de fin: ${currentProject.date_end}",
                            modifier = Modifier.padding(bottom = 5.dp)
                        )
                    }
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
                    TaskItem(
                        item = task,
                        onCheckChange = { isChecked ->
                            // Handle the check change if needed
                        },
                        onEdit = {
                            selectedTask = task
                            showEditDialog = true
                        },
                        onDelete = {
                            taskList.remove(task)
                            saveTasksToFile(context, taskList)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TaskItem(
    item: Tasks,
    onCheckChange: (Boolean) -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val backgroundColor = Color.White
    val borderColor = Color(0xFF6200EE)
    val borderWidth = 1.dp
    val borderRadius = 16.dp

    Column(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(borderRadius))
            .background(backgroundColor)
            .border(borderWidth, borderColor, RoundedCornerShape(borderRadius)),
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.etat,
                    modifier = Modifier.padding(bottom = 4.dp),
                    style = TextStyle(fontWeight = FontWeight.Bold, color = Color(0xFF6200EE)),
                )
                Text(
                    text = item.title,
                    modifier = Modifier.padding(bottom = 4.dp),
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = item.desciption,
                    modifier = Modifier.padding(bottom = 4.dp),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Row {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Filled.Edit, contentDescription = "Edit")
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete")
                }
            }
        }
    }
}
