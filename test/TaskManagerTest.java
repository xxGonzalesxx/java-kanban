import managers.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected abstract T createTaskManager();

    // ТЕСТЫ ДЛЯ TASK
    @Test
    void createTask_shouldAddTask() {
        T manager = createTaskManager();
        Task task = new Task(0, "Test Task", "Description", Status.NEW);

        manager.createTask(task);
        List<Task> tasks = manager.getAllTasks();

        assertEquals(1, tasks.size());
        assertEquals("Test Task", tasks.get(0).getName());
    }

    @Test
    void getTaskById_shouldReturnCorrectTask() {
        T manager = createTaskManager();
        Task task = new Task(0, "Test", "Desc", Status.NEW);
        manager.createTask(task);

        Task retrieved = manager.getTaskById(task.getId());
        assertEquals(task, retrieved);
    }

    // ТЕСТЫ ДЛЯ EPIC
    @Test
    void createEpic_shouldAddEpic() {
        T manager = createTaskManager();
        Epic epic = new Epic(0, "Test Epic", "Description", Status.NEW);

        manager.createEpic(epic);
        List<Epic> epics = manager.getAllEpics();

        assertEquals(1, epics.size());
        assertEquals("Test Epic", epics.get(0).getName());
    }

    // ТЕСТЫ ДЛЯ SUBTASK
    @Test
    void createSubtask_shouldAddSubtaskAndLinkToEpic() {
        T manager = createTaskManager();
        Epic epic = new Epic(0, "Epic", "Desc", Status.NEW);
        manager.createEpic(epic);

        Subtask subtask = new Subtask(0, "Subtask", "Desc", Status.NEW, epic.getId());
        manager.createSubtask(subtask);

        assertEquals(1, manager.getAllSubtasks().size());
        assertTrue(manager.getSubtasksOfEpic(epic.getId()).contains(subtask));
    }

    // НОВЫЕ ТЕСТЫ ДЛЯ 8 СПРИНТА
    @Test
    void getPrioritizedTasks_shouldReturnSortedByStartTime() {
        T manager = createTaskManager();

        Task task1 = new Task(0, "Task 1", "Desc", Status.NEW,
                Duration.ofMinutes(30), LocalDateTime.of(2024, 10, 1, 10, 0));
        Task task2 = new Task(0, "Task 2", "Desc", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2024, 10, 1, 9, 0));

        manager.createTask(task1);
        manager.createTask(task2);

        List<Task> prioritized = manager.getPrioritizedTasks();
        assertEquals("Task 2", prioritized.get(0).getName()); // более ранняя
        assertEquals("Task 1", prioritized.get(1).getName()); // более поздняя
    }

    @Test
    void hasTimeConflict_shouldDetectOverlappingTasks() {
        T manager = createTaskManager();

        Task task1 = new Task(0, "Task 1", "Desc", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2024, 10, 1, 10, 0));
        manager.createTask(task1);

        Task task2 = new Task(0, "Task 2", "Desc", Status.NEW,
                Duration.ofMinutes(30), LocalDateTime.of(2024, 10, 1, 10, 30)); // пересекается

        assertThrows(Exception.class, () -> manager.createTask(task2));
    }

    // Добавь остальные тесты по аналогии...
}