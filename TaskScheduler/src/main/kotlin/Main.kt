package taskScheduler

import java.time.LocalDateTime

fun main() {

    val taskWork: Task = Task(
        title = "Work",
        priority = Priority.HIGH,
        deadline = LocalDateTime.now().plusHours(4),
        estimatedDuration = 3,
        status = Status.NOT_STARTED
    )

    val taskGroceries: Task = Task(
        title = "Groceries",
        priority = Priority.HIGH,
        deadline = LocalDateTime.now().plusHours(3),
        estimatedDuration = 2,
        dependencies = mutableListOf(taskWork),
        status = Status.NOT_STARTED
    )

    val taskCook: Task = Task(
        title = "Cook",
        priority = Priority.MEDIUM,
        deadline = LocalDateTime.now().plusHours(5),
        estimatedDuration = 2,
        dependencies = mutableListOf(taskGroceries),
        status = Status.NOT_STARTED
    )

    val taskClean: Task = Task(
        title = "Clean",
        priority = Priority.LOW,
        estimatedDuration = 1,
        status = Status.NOT_STARTED
    )

    val taskInThePast: Task = Task(
        title = "InThePast",
        priority = Priority.LOW,
        deadline = LocalDateTime.now().minusHours(1),
//        deadline = LocalDateTime.now().plusHours(1),
        estimatedDuration = 1,
        status = Status.NOT_STARTED
    )

    // Artificially create a circular dependency
//    taskWork.dependencies.add(taskCook)

    // Add a dependency in the past
//    taskWork.dependencies.add(taskInThePast)

    val taskManager = TaskManager()
    taskManager.addTask(taskWork)
    taskManager.addTask(taskGroceries)
    taskManager.addTask(taskCook)
    taskManager.addTask(taskClean)

    println("Checking if we can do all due tasks tomorrow:")
    println(taskManager.canDoAllTasks())

//    var recommendedTask: Task?
//    while (true) {
//        recommendedTask = taskManager.recommendNextTask()
//        recommendedTask?.let {
//            println("Recommended task: ${it.title}")
//            taskManager.updateTaskStatus(it, Status.COMPLETED)
//        } ?: break
//    }


}