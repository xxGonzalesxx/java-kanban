import managers.InMemoryHistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private InMemoryHistoryManager historyManager;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    void add_shouldAddTask() {
        Task task = new Task(1, "Task1", "Description1", Status.NEW);
        historyManager.add(task);

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size());
        assertEquals(task, history.get(0));
    }

    @Test
    void add_shouldAddSubtask() {
        Subtask subtask = new Subtask(2, "Subtask1", "Desc", Status.NEW, 10);
        historyManager.add(subtask);

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size());
        assertEquals(subtask, history.get(0));
    }

    @Test
    void getHistory_shouldReturnEmptyListInitially() {
        List<Task> history = historyManager.getHistory();
        assertTrue(history.isEmpty());
    }

    @Test
    void add_shouldMaintainHistoryOrder() {
        Task task1 = new Task(1, "Task1", "Desc1", Status.NEW);
        Task task2 = new Task(2, "Task2", "Desc2", Status.IN_PROGRESS);
        historyManager.add(task1);
        historyManager.add(task2);

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());
        assertEquals(task1, history.get(0));
        assertEquals(task2, history.get(1));
    }

    @Test
    void add_shouldRemoveOldestWhenMoreThan10() {
        for (int i = 1; i <= 11; i++) {
            historyManager.add(new Task(i, "tasks.Task" + i, "Desc" + i, Status.NEW));
        }

        List<Task> history = historyManager.getHistory();
        assertEquals(10, history.size());
        assertEquals(2, history.get(0).getId()); // Первый элемент удалился
        assertEquals(11, history.get(9).getId()); // Последний элемент добавился
    }
}
