package taskScheduler

import java.time.LocalTime
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TaskManager {
    private val tasks = mutableListOf<Task>()

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
        // Update the Status of a task
        task.status = newStatus
        println("Updated status of task '${task.title}' to $newStatus")
    }

    private fun checkCircularDependency(taskToAdd: Task) {
        // Checking if a task to add will create a circular dependency
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
        /*
        Recommend the next task based on:
        - Priority
        - Estimated Duration
        If no task are available or all ar COMPLETED, return null
         */
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

    private fun allocateTime(task: Task, tasksAlreadyAllocated: MutableSet<Task>,  remainingHours: Int): Int {
        /*
        Helper to allocate time for a task while checking if we have enough time left
         */

        // If that task has already been allocated, skip
        if (tasksAlreadyAllocated.contains(task)) {
            return remainingHours
        }

        val remainingHoursNew = remainingHours - task.estimatedDuration
        tasksAlreadyAllocated.add(task)
        println(">> Add task ${task.title} with duration ${task.estimatedDuration} to schedule. Remaining hours: $remainingHoursNew")
        return remainingHoursNew
    }

    fun canDoAllTasks(): Boolean {
        /*
        There are different things we have to check
        - Do not consider tasks that have the deadline further than next day + 8 hours away
        - Do not consider tasks that have dependencies that cannot be completed in time
         */

        val WORKDAY_HOURS = 8
        // Get the timeframe we have to consider. Next day 08:00 till 16:00
        // Start time: next day at 08:00
        val startOfNextDay = LocalDateTime.now()
            .plusDays(1)
            .with(LocalTime.of(8, 0)) // Set to 08:00

        // End time: same next day at 16:00
        val endOfNextDay = startOfNextDay.plusHours(WORKDAY_HOURS.toLong())
        var remainingHours = WORKDAY_HOURS

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val formattedStart = startOfNextDay.format(formatter)
        val formattedEnd = endOfNextDay.format(formatter)

        println("Checking if we can do all due tasks between $formattedStart and $formattedEnd")
        println("Available hours: $remainingHours")

        // Keep track of which tasks we have already subtracted from our timebudget
        var tasksAlreadyCounted: MutableSet<Task> = mutableSetOf<Task>()

        for (task in tasks) {

            println("Checking task: ${task.title}")

            // If already time for it allocated skip
            if (tasksAlreadyCounted.contains(task)) {
                println(">> Task ${task.title} is already allocated, we can skip it")
                continue
            }

            // If already completed skip
            if (task.status == Status.COMPLETED) {
                println(">> Task ${task.title} is already done, we dont care")
                continue
            }

            // If deadline is not set skip
            if (task.deadline == null) {
                println(">> Task ${task.title} has no deadline, we dont care")
                continue
            }

            // If deadline is further than our next workday skip
            if (task.deadline!! > endOfNextDay) {
                println(">> Task ${task.title} has a deadline further than tomorrow end of work day, we dont care")
                continue
            }

            // If task has no dependencies, subtract duration from remaining hours and go on
            if (task.dependencies.isEmpty()) {
                println(">> Task ${task.title} is not completed has no dependencies, we have to allocate time for it")
                remainingHours = allocateTime(task, tasksAlreadyCounted, remainingHours)
                if (remainingHours < 0) {
                    println("ERROR: Task ${task.title} pushes us over the limit, we cant do all tasks in time")
                    return false
                }
                continue
            }

            // If task has dependencies, check if all are COMPLETED. If all completed, subtract duration from remaining hours
            if (task.dependencies.all { it.status == Status.COMPLETED }) {
                println(">> Task ${task.title} has dependencies which are all COMPLETED, we have to allocate time for it")
                remainingHours = allocateTime(task, tasksAlreadyCounted, remainingHours)
                if (remainingHours < 0) {
                    println("ERROR: Task ${task.title} pushes us over the limit, we cant do all tasks in time")
                    return false
                }
                continue
            }

            // If task has dependencies that are not COMPLETED and the deadline is already over, return false, cant solve
            for (dep in task.dependencies) {
                if (dep.status != Status.COMPLETED && dep.deadline!! <= startOfNextDay) {
                    println("ERROR: Task '${task.title}' has a dependency '${dep.title}' that is not COMPLETED and has a deadline before tomorrow morning, cant complete all tasks!")
                    return false
                }
            }

            // If task has dependencies that are not COMPLETED and the deadline is not over yet, allocate them as well
            for (dep in task.dependencies) {
                if (dep.status != Status.COMPLETED && !tasksAlreadyCounted.contains(dep)) {
                    println(">> Task ${task.title} has dependency ${dep.title} that is not COMPLETED, we have to allocate time for it first")
                    remainingHours = allocateTime(dep, tasksAlreadyCounted, remainingHours)
                    if (remainingHours < 0) {
                        println("ERROR: Task ${dep.title} pushes us over the limit, we cant do all tasks in time")
                        return false
                    }
                    continue
                }
            }

            // Finally allocate the task itself
            remainingHours = allocateTime(task, tasksAlreadyCounted, remainingHours)
            if (remainingHours < 0) {
                println("ERROR: Task ${task.title} pushes us over the limit, we cant do all tasks in time")
                return false
            }
        }
        println("SUCCESS: All tasks can be done in time! With $remainingHours hours left")
        return true
    }
}
