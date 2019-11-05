package com.example

import java.time.LocalDateTime

class Task(
    var title: String,
    var description: String,
    var createdDateTime: LocalDateTime,
    var updatedDateTime: LocalDateTime,
    var id: Long?,
    var isCompleted: Boolean
) {
}