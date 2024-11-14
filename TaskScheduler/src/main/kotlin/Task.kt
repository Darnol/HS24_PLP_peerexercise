package taskScheduler

import java.time.LocalDateTime

data class Task(
    val title: String,
    val priority: Priority,
    val deadline: LocalDateTime? = null,
    val dependencies: MutableList<Task> = mutableListOf(),
    val estimatedDuration: Int,
    var status: Status = Status.NOT_STARTED
) {
    override fun hashCode(): Int {
        // Keep it simple, only use title. Dependencies create StackOverflowError when used to compare
        return title.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Task) return false

        return title == other.title
    }
}

enum class Priority { HIGH, MEDIUM, LOW }
enum class Status { NOT_STARTED, IN_PROGRESS, COMPLETED }