package taskScheduler

import java.time.LocalDateTime

fun main() {
    val taskGroceries: Task = Task(
        title = "Groceries",
        priority = Priority.HIGH,
        deadline = LocalDateTime.now().plusHours(3),
        estimatedDuration = 2,
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

//    println(taskGroceries)
//    println(taskCook)
//    println(taskClean)

    val taskManager = TaskManager()
    taskManager.addTask(taskGroceries)
    taskManager.addTask(taskCook)
    taskManager.addTask(taskClean)

    taskManager.printTasks()

    taskManager.updateTaskStatus(taskGroceries, Status.IN_PROGRESS)

    taskManager.printTasks()



}