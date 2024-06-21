package com.example.myapplication

import android.content.Context
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.ui.theme.MyApplicationTheme

@Composable
fun ProjectsScreen(context: Context, navController: NavController, userId: Int) {
    val currentUserId = 1;
    val projects = loadAllProjectDataByUserId(context, userId);

    LazyColumn(modifier = Modifier.padding(16.dp,32.dp)) {
        item {
            Text(
                text = "Mes projets",
                style = MaterialTheme.typography.h4.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        items(projects!!.projects){ project ->
            ProjectItem(project = project, navController, userId);
        }
    }
}

@Composable
fun ProjectItem(project: Project, navController: NavController, userId: Int) {
    Box(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .border(
                BorderStroke(1.dp, Color(0xFF6200EE)),
                MaterialTheme.shapes.medium
            )
            .clickable {
                navController.navigate("todoPage/${userId}")
            }
    ) {
        Column(modifier = Modifier.padding(16.dp)){
            Text(
                text = project.title,
                style = MaterialTheme.typography.h6.copy(
                    color = Color(0xFF6200EE),
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = project.descritpion,
                modifier = Modifier.padding(bottom = 5.dp)
            )
            Text(
                text =  "Date de début: ${project.date_start}",
                modifier = Modifier.padding(bottom = 5.dp)
            )
            Text(
                text =  "Date de fin: ${project.date_end}",
                modifier = Modifier.padding(bottom = 5.dp)
            )
        }
    }
}

@Composable
fun PreviewProjects() {

}

@Preview(showBackground = true)
@Composable
fun ProjectsPreview() {
    MyApplicationTheme {
        PreviewProjects()
    }
}