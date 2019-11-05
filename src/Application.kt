package com.example

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import io.ktor.application.*
import io.ktor.features.CORS
import io.ktor.features.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.jackson.jackson
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    Database.connect(
        url    = "jdbc:h2:file:./database",
        driver = "org.h2.Driver"
    )
    transaction {
        SchemaUtils.create(TaskTable)
    }
    install(CORS) {
        anyHost()
        method(HttpMethod.Patch)
        header(HttpHeaders.ContentType)
    }
    install(ContentNegotiation) {
        jackson {
            propertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE
            registerModule(JavaTimeModule().apply {
                addSerializer(LocalDateTime::class.java, LocalDateTimeSerializer(DateTimeFormatter.ISO_DATE_TIME))
                addDeserializer(LocalDateTime::class.java, LocalDateTimeDeserializer(DateTimeFormatter.ISO_DATE_TIME))
            })
        }
    }

//    val taskRepository = DummyTaskRepository()
    val taskRepository = ExposedTaskRepository()
    val taskController = TaskController(taskRepository)

    routing {
        route("tasks") {
            get() {
                taskController.index(call)
            }
            post() {
                taskController.create(call)
            }
            get("/{id}") {
                taskController.show(call)
            }
            patch("/{id}") {
                taskController.update(call)
            }
        }
    }

}

