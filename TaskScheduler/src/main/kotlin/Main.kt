package taskScheduler

import java.time.LocalDateTime
import java.time.LocalTime

fun main() {

    val startTomorrow = LocalDateTime.now()
        .plusDays(1)
        .with(LocalTime.of(8, 0)) // Set to 08:00

    val taskWork: Task = Task(
        title = "Work",
        priority = Priority.HIGH,
        deadline = startTomorrow.plusHours(1),
        estimatedDuration = 3,
        status = Status.NOT_STARTED
    )

    val taskGroceries: Task = Task(
        title = "Groceries",
        priority = Priority.HIGH,
        deadline = startTomorrow.plusHours(4),
        estimatedDuration = 2,
        dependencies = mutableListOf(taskWork),
        status = Status.NOT_STARTED
    )

    val taskCook: Task = Task(
        title = "Cook",
        priority = Priority.MEDIUM,
        deadline = startTomorrow.plusHours(6),
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
        estimatedDuration = 1,
        status = Status.NOT_STARTED
    )

    // This throws an error, cant add tasks with deadlines in the past
//    taskWork.dependencies.add(taskInThePast)

    // This would create a circular dependency. Cant add that task afterwards
//    taskWork.dependencies.add(taskCook)

    // Add the tasks to the manager
    val taskManager = TaskManager()
    taskManager.addTask(taskWork)
    taskManager.addTask(taskGroceries)
    taskManager.addTask(taskCook)
    taskManager.addTask(taskClean)

    println("\n---------------------------------------------------------------------------")
    taskManager.canDoAllTasks()

    println("\n---------------------------------------------------------------------------")
    println("Recommending your next tasks:")
    var recommendedTask: Task?
    while (true) {
        recommendedTask = taskManager.recommendNextTask()
        recommendedTask?.let {
            println("Recommended task: ${it.title}")
            taskManager.updateTaskStatus(it, Status.COMPLETED)
        } ?: break
    }
}