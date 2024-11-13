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
    val taskManager = TaskManager()
    taskManager.addTask(taskGroceries)
    taskManager.addTask(taskCook)
    taskManager.addTask(taskClean)

    var recommendedTask: Task? = taskManager.recommendNextTask()
    recommendedTask?.let {
        println("Recommended task: ${it.title}. Setting it complete")
        taskManager.updateTaskStatus(it, Status.COMPLETED)
    }

    recommendedTask = taskManager.recommendNextTask()
    recommendedTask?.let {
        println("Recommended task: ${it.title}. Setting it complete")
    }

}