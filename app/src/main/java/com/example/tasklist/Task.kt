package com.example.tasklist

import java.io.Serializable

data class Task(
    val title : String,
    val description : String,
    var completed : Boolean = false
): Serializable
