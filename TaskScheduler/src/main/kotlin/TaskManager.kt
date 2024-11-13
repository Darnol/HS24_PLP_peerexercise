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

    fun recommendNextTask(): Task? {
        if (tasks.isEmpty()) {
            println("No tasks in list, cannot recommend anything")
        }
        return tasks
            .filter { it.status == Status.NOT_STARTED && it.dependencies.all { dep -> dep.status == Status.COMPLETED } }
            .sortedWith(compareBy( { it.priority }, { it.estimatedDuration }))
            .firstOrNull()
    }

    fun canDoAllTasks(): Boolean {
        // TODO: Given an 8h workday, can we do them all?
        return false
    }
}
