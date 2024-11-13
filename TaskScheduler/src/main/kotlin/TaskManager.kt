package taskScheduler

class TaskManager {
    private val tasks = mutableListOf<Task>()

    fun printTasks() {
        tasks.forEach { println(it) }
    }

    fun addTask(task: Task) {
        checkCircularDependency(task)
        tasks.add(task)
        println("Added task: ${task.title}")
    }

    fun removeTask(task: Task) {
        tasks.remove(task)
        println("Removed task: ${task.title}")
    }

    fun updateTaskStatus(task: Task, newStatus: Status) {
        task.status = newStatus
        println("Updated status of task '${task.title}' to $newStatus")
    }

    private fun checkCircularDependency(taskToAdd: Task) {
        // TODO: Before adding a task, check if it creates a circular dependency
    }

    fun recommendNextTask() {
        // TODO: Based on priorities
        // 1. Return the task with the highest priority if possible
        // 2. If not possible, either task with second highest priority or the one with the earliest deadline
    }

    fun canDoAllTasks(): Boolean {
        // TODO: Given an 8h workday, can we do them all?
        return false
    }
}
