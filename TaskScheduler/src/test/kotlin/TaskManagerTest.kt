import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.TestInstance
import taskScheduler.Priority
import taskScheduler.Status
import kotlin.test.BeforeTest

import taskScheduler.Task
import taskScheduler.TaskManager
import java.time.LocalDateTime

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class TaskManagerTest {

    private lateinit var tasks: listOf(Task)

    @BeforeTest
    fun setUp() {
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
        tasks.
    }

    @Test
    fun addTask() {
    }

    @Test
    fun removeTask() {
    }

    @Test
    fun updateTaskStatus() {
    }
}