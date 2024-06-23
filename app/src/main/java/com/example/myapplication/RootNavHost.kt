package com.example.myapplication;

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument


@Composable
fun RootNavHost(context: Context) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login") {
        composable("login") { Login(navController = navController, context = context) }
        composable(
            route = "todoPage/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) {
            val userId = it.arguments?.getInt("userId")
            userId?.let { userId ->
                TaskList(navController, userId)
            } ?: run {

            }
        }
        composable(
            route = "projects/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) {
            val userId = it.arguments?.getInt("userId")
            userId?.let { userId ->
                ProjectsScreen(context, navController, userId)
            } ?: run {
            }
        }
    }
}