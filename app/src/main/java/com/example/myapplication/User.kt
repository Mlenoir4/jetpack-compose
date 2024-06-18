package com.example.myapplication

import com.google.gson.annotations.SerializedName
import java.util.Date

data class User(
    @SerializedName("id") val id: Int,
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)data class UsersList(
    @SerializedName("Users") val users: List<User>
)

data class Tasks(
    @SerializedName("id") val id: Int,
    @SerializedName("project_id") val project_id: Int,
    @SerializedName("etat") val etat: String,
    @SerializedName("title") val title: String,
    @SerializedName("desciption") val desciption: String,
)
data class TasksList(
    @SerializedName("Tasks") val tasks: List<Tasks>
)

data class Project(
    @SerializedName("id") val id: Int,
    @SerializedName("owner_id") val owner_id: Int,
    @SerializedName("users_acces") val users_acces: List<Int>,
    @SerializedName("title") val title: String,
    @SerializedName("descritpion") val descritpion: String,
    @SerializedName("date_start") val date_start: Date,
    @SerializedName("date_end") val date_end: Date,
    @SerializedName("tasks") val tasks: List<Int>
)

data class ProjectsList(
    @SerializedName("Projects") val projects: List<Project>
)