package taskScheduler

class TaskManager {
    private val tasks = mutableListOf<Task>()

    fun getTasks(): MutableList<Task> {
        return tasks
    }

    fun printTasks() {
        tasks.forEach { println(it) }
    }

    fun countTasks(): Int {
        return tasks.size
    }

    fun addTask(task: Task) {
        // Don't allow duplicate task titles
        if (task.title in tasks.map { it.title }) {
            println("Task with title '${task.title}' already exists")
            throw IllegalArgumentException("A task with the title '${task.title}' already exists.")
        }
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
