import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    void add_shouldNotAllowDuplicates() {
        Task task = new Task(1, "Task1", "Desc", Status.NEW);

        historyManager.add(task);
        historyManager.add(task); // второй раз та же задача

        List<Task> history = historyManager.getHistory();

        assertEquals(1, history.size(), "История не должна содержать дубликаты");
        assertEquals(task, history.get(0), "В истории должна остаться только одна задача");
    }
    @Test
    void add_shouldMoveTaskToEndIfAlreadyPresent() {
        Task task1 = new Task(1, "Task1", "Desc1", Status.NEW);
        Task task2 = new Task(2, "Task2", "Desc2", Status.NEW);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task1); // снова добавляем task1

        List<Task> history = historyManager.getHistory();

        assertEquals(2, history.size(), "История должна содержать 2 уникальные задачи");
        assertEquals(task2, history.get(0), "Task2 должен быть первым");
        assertEquals(task1, history.get(1), "Task1 должен переместиться в конец");
    }
    @Test
    void remove_shouldDeleteTaskFromHistory() {
        Task task1 = new Task(1, "Task1", "Desc1", Status.NEW);
        Task task2 = new Task(2, "Task2", "Desc2", Status.NEW);

        historyManager.add(task1);
        historyManager.add(task2);

        historyManager.remove(1); // удаляем первую задачу

        List<Task> history = historyManager.getHistory();

        assertEquals(1, history.size(), "В истории должна остаться только одна задача");
        assertEquals(task2, history.get(0), "После удаления task1 должна остаться только task2");
    }
}
