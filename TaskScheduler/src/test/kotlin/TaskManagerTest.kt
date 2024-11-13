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

    private lateinit var tasks: MutableList<Task>
    private lateinit var taskManager: TaskManager

    @BeforeTest
    fun setUp() {

        taskManager = TaskManager()

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
        tasks = mutableListOf()
        tasks.add(taskGroceries)
        tasks.add(taskCook)
        tasks.add(taskClean)
    }

    @Test
    fun addTask() {
        assertEquals(taskManager.countTasks(), 0)
        taskManager.addTask(tasks[0])
        assertEquals(taskManager.countTasks(), 1)

        assertThrows(IllegalArgumentException::class.java) {
            taskManager.addTask(tasks[0])
        }
    }

    @Test
    fun removeTask() {
        taskManager.addTask(tasks[0])
        assertEquals(taskManager.countTasks(), 1)
        taskManager.removeTask(tasks[0])
        assertEquals(taskManager.countTasks(), 0)
    }

    @Test
    fun removeTaskDependency() {
        taskManager.addTask(tasks[0])
        taskManager.addTask(tasks[1])
        taskManager.removeTask(tasks[0])
        assertEquals(tasks[1].dependencies.size, 0)
    }

    @Test
    fun updateTaskStatus() {
        val nameTaskToUpdate: String = tasks[0].title
        taskManager.addTask(tasks[0])
        assertEquals(tasks[0].status, Status.NOT_STARTED)
        taskManager.updateTaskStatus(tasks[0], Status.IN_PROGRESS)
        assertEquals(tasks[0].status, Status.IN_PROGRESS)
        val taskRetreived = taskManager.getTasks().firstOrNull { it.title == nameTaskToUpdate }
        assertNotNull(taskRetreived)
        assertEquals(taskRetreived?.status, Status.IN_PROGRESS)
    }


}