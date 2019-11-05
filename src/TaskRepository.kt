package com.example

interface TaskRepository {
    fun findAll(): List<Task>
    fun insertTask(task: Task)
    fun findById(id: Long): Task?
    fun updateTask(task: Task)
}