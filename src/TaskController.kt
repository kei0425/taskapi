package com.example

import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import java.time.LocalDateTime

class TaskController(val taskRepository: TaskRepository) {
    suspend fun index(call: ApplicationCall) {
        val tasks = taskRepository.findAll()
        call.respond(tasks)
    }

    suspend fun create(call: ApplicationCall) {
        val body = call.receive<TaskCreateBody>()
        val task = Task(
            id = null,
            title = body.title,
            description = body.description,
            createdDateTime = LocalDateTime.now(),
            updatedDateTime = LocalDateTime.now(),
            isCompleted = false
        )
        taskRepository.insertTask(task)
        call.response.status(HttpStatusCode.Created)
    }

    suspend fun show(call: ApplicationCall) {
        val id = call.parameters["id"]?.toLongOrNull()
        val task = id?.let{ taskRepository.findById(it) }

        if (task == null) {
            call.response.status(HttpStatusCode.NotFound)
        } else {
            call.respond(task)
        }
    }

    suspend fun update(call: ApplicationCall) {
        val id = call.parameters["id"]?.toLongOrNull()
        val task = id?.let{ taskRepository.findById(it) }

        if (task == null) {
            call.response.status(HttpStatusCode.NotFound)
            return
        }
        val body = call.receive<TaskUpdateBody>()

        task.title = body.title
        task.description = body.description
        task.isCompleted = body.isCompleted
        task.updatedDateTime = LocalDateTime.now()

        taskRepository.updateTask(task)
        call.response.status(HttpStatusCode.NoContent)
    }
}