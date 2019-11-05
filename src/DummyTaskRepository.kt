package com.example

import java.time.LocalDateTime

class DummyTaskRepository: TaskRepository {
    override fun findAll(): List<Task> = listOf(
        Task(
            "Foo",
            "hogehoge",
            LocalDateTime.now(),
            LocalDateTime.now(),
            123,
            false
        ),
        Task(
            "bar",
            "fugafuga",
            LocalDateTime.now(),
            LocalDateTime.now(),
            125,
            true
        )
    )

    override fun insertTask(task: Task) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findById(id: Long): Task? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateTask(task: Task) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}