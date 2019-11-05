package com.example

import org.jetbrains.exposed.dao.LongIdTable
import org.jetbrains.exposed.sql.Column
import org.joda.time.DateTime

object TaskTable: LongIdTable() {
    val title: Column<String> = text("title")
    val description: Column<String> = text("description")
    val createdDateTime: Column<DateTime> = datetime("created_date_time")
    val updatedDateTime: Column<DateTime> = datetime("updated_date_time")
    val isCompleted: Column<Boolean> = bool("completed")
}
