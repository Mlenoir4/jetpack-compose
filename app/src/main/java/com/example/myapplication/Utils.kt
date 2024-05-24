package com.example.myapplication;

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

fun loadUsersFromAsset(context: Context, fileName: String): List<User>? {
    return try {
        val jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        val userType = object : TypeToken<List<User>>() {}.type
        Gson().fromJson(jsonString, userType)
    } catch (ioException: IOException) {
        ioException.printStackTrace()
        null
    }
}

fun validateUser(username: String, password: String, users: List<User>): Boolean {
    return users.any { it.username == username && it.password == password }
}
