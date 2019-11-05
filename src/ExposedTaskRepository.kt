package com.example

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import java.time.LocalDateTime
import java.time.ZoneId

class ExposedTaskRepository: TaskRepository {
    private fun DateTime.toStandardLocalDateTime(): LocalDateTime {
        return LocalDateTime.ofInstant(toDate().toInstant(), ZoneId.systemDefault())
    }

    private fun LocalDateTime.toJodaDateTime(): DateTime {
        return DateTime(atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
    }

    override fun findAll(): List<Task> = transaction {
        TaskTable.selectAll()
            .orderBy(TaskTable.createdDateTime, SortOrder.DESC)
            .map {
                toTask(it)
            }
    }

    override fun insertTask(task: Task) {
        transaction {
            TaskTable.insert {
                it[title] = task.title
                it[description] = task.description
                it[isCompleted] = task.isCompleted
                it[createdDateTime] = task.createdDateTime.toJodaDateTime()
                it[updatedDateTime] = task.updatedDateTime.toJodaDateTime()
            }
        }
    }

    override fun findById(id: Long): Task?  = transaction {
        TaskTable.select { TaskTable.id eq id }
            .map {
                toTask(it)
            }
            .firstOrNull()
    }

    fun toTask(it: ResultRow): Task {
        return Task(
            id = it[TaskTable.id].value,
            title = it[TaskTable.title],
            description = it[TaskTable.description],
            createdDateTime = it[TaskTable.createdDateTime].toStandardLocalDateTime(),
            updatedDateTime = it[TaskTable.updatedDateTime].toStandardLocalDateTime(),
            isCompleted = it[TaskTable.isCompleted]
        )
    }

    override fun updateTask(task: Task) {
        transaction {
            TaskTable.update({ TaskTable.id eq task.id}) {
                it[title] = task.title
                it[description] = task.description
                it[isCompleted] = task.isCompleted
                it[createdDateTime] = task.createdDateTime.toJodaDateTime()
                it[updatedDateTime] = task.updatedDateTime.toJodaDateTime()
            }
        }
    }
}