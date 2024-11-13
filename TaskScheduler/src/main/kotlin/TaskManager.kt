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
//        checkCircularDependency(task)
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

//    private fun checkCircularDependency(taskToAdd: Task) {
//        // Start a DFS from the task to be added, passing an empty set as the initial visited set
//        if (hasCircularDependency(taskToAdd, mutableSetOf())) {
//            throw IllegalArgumentException("Adding '${taskToAdd.title}' would create a circular dependency.")
//        }
//    }
//
//    private fun hasCircularDependency(task: Task, visited: MutableSet<Task>): Boolean {
//        // If the task is already in the visited set, we have a circular dependency
//        if (task in visited) {
//            return true
//        }
//
//        // Add the task to the visited set
//        visited.add(task)
//
//        // Recursively check each dependency of the task
//        for (dependency in task.dependencies) {
//            if (hasCircularDependency(dependency, visited)) {
//                return true
//            }
//        }
//
//        // Remove the task from the visited set after processing
//        visited.remove(task)
//        return false
//    }

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
