package taskScheduler

import java.time.LocalDateTime
import java.time.LocalTime

fun main() {

    // First get the start of the day tomorrow
    val startTomorrow = LocalDateTime.now()
        .plusDays(1)
        .with(LocalTime.of(8, 0)) // Set to 08:00

    // Create some task, some depend on each other
    // Work -> Groceries -> Cook
    // Clean is independent
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

    // This task will be added and removed to showcase functionality
    val taskIrrelevant: Task = Task(
        title = "Irrelevant",
        priority = Priority.LOW,
        estimatedDuration = 1,
        status = Status.NOT_STARTED
    )

    // Also create a task with a deadline in the past to showcase error handling
    val taskInThePast: Task = Task(
        title = "InThePast",
        priority = Priority.LOW,
        deadline = LocalDateTime.now().minusHours(1),
        estimatedDuration = 1,
        status = Status.NOT_STARTED
    )

    // Create a TaskManager instance to manage our tasks
    val taskManager = TaskManager()

    // Before adding our tasks to the manager, go through two scenarios which are prohibited:

    // 1. Add a task with a dependency deadline in the past
//    taskWork.dependencies.add(taskInThePast) // If uncommented, taskManager.addTask(taskWork) will throw error

    // 2. Add task that creates circular dependency
//    taskWork.dependencies.add(taskCook) // If uncommented, taskManager.addTask(taskWork) will throw error

    // Ignoring the error cases, we add our 4 tasks to the manager
    taskManager.addTask(taskGroceries)
    taskManager.addTask(taskCook)
    taskManager.addTask(taskWork)
    taskManager.addTask(taskClean)

    // We can also add and remove a task
    taskManager.addTask(taskIrrelevant)
    taskManager.removeTask(taskIrrelevant)

    println("\n---------------------------------------------------------------------------")
    /*
     Addressing the function:
        "Calculate whether all deadlines can be met given estimated durations and assuming an
        8h work day, starting work on the next calendar day."

     canDoAllTasks will check if all tasks can be done, i.e. no circular dependencies and no dependencies with deadline in the past
     It will not change the status of any task
     */
    taskManager.canDoAllTasks()


    println("\n---------------------------------------------------------------------------")
    /*
    Addressing the functions:
         "Get a next recommended task based on priorities and dependencies"
         "Calculate an optimal sequence of tasks considering deadlines and dependencies"
     */

    // recommendNextTask
    println("Recommending your next tasks and completing them:")
    var recommendedTask: Task?
    // Keep recommending tasks until there are no more tasks to recommend
    while (true) {
        recommendedTask = taskManager.recommendNextTask()
        recommendedTask?.let {
            println("Recommended task: ${it.title}")
            taskManager.updateTaskStatus(it, Status.COMPLETED)
        } ?: break
    }
}