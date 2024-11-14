package taskScheduler

import java.time.LocalDateTime

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
            throw IllegalArgumentException("A task with the title '${task.title}' already exists.")
        }
        // Don't allow tasks with deadlines in the past
        task.deadline?.let {
            if (task.deadline <= LocalDateTime.now()) {
                throw IllegalArgumentException("A task cannot have a deadline in the past")
            }
        }
        // Don't allow dependencies which are not COMPLETED to have a deadline in the past
        for (dep in task.dependencies) {
            if (dep.status != Status.COMPLETED && dep.deadline != null && dep.deadline <= LocalDateTime.now()) {
                throw IllegalArgumentException("A task cannot have dependencies which are not COMPLETED and that have a deadline in the past")
            }
        }

        checkCircularDependency(task)
        tasks.add(task)
        println("Added task: ${task.title}")
    }

    fun removeTask(task: Task) {
        // Remove it as dependency
        tasks.forEach { it.dependencies.remove(task) }

        // Remove the task itself
        tasks.remove(task)
        println("Removed task: ${task.title}")
    }

    fun updateTaskStatus(task: Task, newStatus: Status) {
        task.status = newStatus
        println("Updated status of task '${task.title}' to $newStatus")
    }

    private fun checkCircularDependency(taskToAdd: Task) {
        if (hasCircularDependency(taskToAdd, mutableSetOf())) {
            throw IllegalArgumentException("Adding '${taskToAdd.title}' would create a circular dependency.")
        }
    }

    private fun hasCircularDependency(taskToAdd: Task, visited: MutableSet<Task>): Boolean {

        if (taskToAdd.dependencies.isEmpty()) {
            return false
        }

        for (dependency in taskToAdd.dependencies) {
            if (visited.contains(dependency)) {
                return true
            }
            visited.add(dependency)
            if (hasCircularDependency(dependency, visited)) {
                return true
            }
        }

        return false
    }

    fun recommendNextTask(): Task? {
        if (tasks.isEmpty()) {
            println("No tasks in list, cannot recommend anything")
        }
        val foundTask = tasks
            .filter { it.status == Status.NOT_STARTED && it.dependencies.all { dep -> dep.status == Status.COMPLETED } }
            .sortedWith(compareBy( { it.priority }, { it.estimatedDuration }))
            .firstOrNull()
        if (foundTask == null) {
            println("No tasks available to recommend")
        }
        return foundTask
    }

    fun canDoAllTasks(): Boolean {
        // TODO: Given an 8h workday, can we do them all?
        return false
    }
}
