package com.example.myapplication;

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import java.io.InputStream

fun loadJsonFromAsset(context: Context, fileName: String): String? {

    return try {
        val inputStream: InputStream = context.assets.open(fileName)
        inputStream.bufferedReader().use { it.readText() }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

inline fun <reified T>parseJson(jsonString: String): T {
    return Gson().fromJson(jsonString, T::class.java)
}

fun validateUser(context: Context, username: String, password: String): Any {
    val jsonContent = loadJsonFromAsset(context, "users.json")
    jsonContent?.let {
        val usersList = parseJson<UsersList>(it)
        for (user in usersList.users) {
            Log.v("TAG", "user: $user" )
            if (user.username == username && user.password == password) {
                return user
            }
        }
    }
    return false
}

fun loadUserDataById(context: Context, userId: Int): User? {

    val jsonContent = loadJsonFromAsset(context, "users.json")
    val usersList:UsersList? = jsonContent?.let { parseJson(it) }
    if (usersList != null) {
        return usersList.users.find { it.id == userId }
    }
    return null
}

fun loadProjectDataByUserId(context: Context, projectId: Int): Project? {

        val jsonContent = loadJsonFromAsset(context, "projects.json")
        val projectsList = jsonContent?.let { parseJson<ProjectsList>(it) }
        if (projectsList != null) {
            return projectsList.projects.find { it.owner_id == projectId }
        }
        return null
}

fun loadAllProjectDataByUserId(context: Context, userId: Int): ProjectsList? {
    val jsonContent = loadJsonFromAsset(context, "projects.json")
    val projectsList = jsonContent?.let { parseJson<ProjectsList>(it) }
    if (projectsList != null) {
        return ProjectsList(projectsList.projects.filter { it.owner_id == userId })
    }
    return null
}
fun loadTaskDataByProjectId(context: Context, projectId: Int): TasksList? {
    val jsonContent = loadJsonFromAsset(context, "tasks.json")
    val tasksList = jsonContent?.let { parseJson<TasksList>(it) }
    if (tasksList != null) {
        return TasksList(tasksList.tasks.filter { it.project_id == projectId })
    }
    return null
}
