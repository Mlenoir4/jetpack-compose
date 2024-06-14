package com.example.myapplication;

import android.content.Context
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

fun parseJson(jsonString: String): UsersList {
    return Gson().fromJson(jsonString, UsersList::class.java)
}

fun validateUser(context: Context, username: String, password: String): Any {
    val jsonContent = loadJsonFromAsset(context, "users.json")
    jsonContent?.let {
        val usersList = parseJson(it)
        for (user in usersList.users) {
            if (user.username == username && user.password == password) {
                return user
            }
        }
    }
    return false
}

fun loadUserDataById(context: Context, userId: Int): User? {

    val jsonContent = loadJsonFromAsset(context, "users.json")
    val usersList = jsonContent?.let { parseJson(it) }
    if (usersList != null) {
        return usersList.users.find { it.id == userId }
    }
    return null
}
