package taskScheduler

import java.time.LocalDateTime

data class Task(
    val title: String,
    val priority: Priority,
    val deadline: LocalDateTime? = null,
    val dependencies: MutableList<Task> = mutableListOf(),
    val estimatedDuration: Int,
    var status: Status = Status.NOT_STARTED
) {}

enum class Priority { HIGH, MEDIUM, LOW }
enum class Status { NOT_STARTED, IN_PROGRESS, COMPLETED }