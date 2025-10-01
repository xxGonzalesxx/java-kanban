import managers.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    private InMemoryTaskManager manager;

    @BeforeEach
    void setUp() {
        manager = new InMemoryTaskManager();
    }

    @Test
    void getAllTasks() {
        assertTrue(manager.getAllTasks().isEmpty());

        Task task = new Task(1, "tasks.Task 1", "Desc 1", Status.NEW);
        manager.createTask(task);

        List<Task> tasks = manager.getAllTasks();
        assertEquals(1, tasks.size());
        assertEquals(task, tasks.get(0));
    }

    @Test
    void getHistory() {
        assertTrue(manager.getHistory().isEmpty());

        Task task = new Task(1, "tasks.Task 1", "Desc 1", Status.NEW);
        manager.createTask(task);
        manager.getTaskById(1);

        List<Task> history = manager.getHistory();
        assertEquals(1, history.size());
        assertEquals(task, history.get(0));
    }

    @Test
    void deleteAllTasks() {
        Task task = new Task(1, "tasks.Task 1", "Desc 1", Status.NEW);
        manager.createTask(task);

        manager.deleteAllTasks();
        assertTrue(manager.getAllTasks().isEmpty());
        assertTrue(manager.getHistory().isEmpty());
    }

    @Test
    void getTaskById() {
        Task task = new Task(1, "tasks.Task 1", "Desc 1", Status.NEW);
        manager.createTask(task);

        Task retrieved = manager.getTaskById(1);
        assertEquals(task, retrieved);
    }

    @Test
    void createTask() {
        Task task = new Task(1, "tasks.Task 1", "Desc 1", Status.NEW);
        manager.createTask(task);

        assertEquals(1, manager.getAllTasks().size());
        assertEquals(task, manager.getTaskById(1));
    }

    @Test
    void updateTask() {
        Task task = new Task(1, "tasks.Task 1", "Desc 1", Status.NEW);
        manager.createTask(task);

        Task updated = new Task(1, "Updated", "Updated desc", Status.IN_PROGRESS);
        manager.updateTask(updated);

        Task retrieved = manager.getTaskById(1);
        assertEquals(updated.getName(), retrieved.getName());
        assertEquals(updated.getDescription(), retrieved.getDescription());
        assertEquals(updated.getStatus(), retrieved.getStatus());
    }

    @Test
    void deleteTaskById() {
        Task task = new Task(1, "tasks.Task 1", "Desc 1", Status.NEW);
        manager.createTask(task);

        manager.deleteTaskById(1);
        assertTrue(manager.getAllTasks().isEmpty());
    }

    @Test
    void getAllEpics() {
        Epic epic = new Epic(1, "tasks.Epic 1", "Desc 1", Status.NEW);
        manager.createEpic(epic);

        List<Epic> epics = manager.getAllEpics();
        assertEquals(1, epics.size());
        assertEquals(epic, epics.get(0));
    }

    @Test
    void deleteAllEpics() {
        Epic epic = new Epic(1, "tasks.Epic 1", "Desc 1", Status.NEW);
        manager.createEpic(epic);

        manager.deleteAllEpics();
        assertTrue(manager.getAllEpics().isEmpty());
    }

    @Test
    void getEpicById() {
        Epic epic = new Epic(1, "tasks.Epic 1", "Desc 1", Status.NEW);
        manager.createEpic(epic);

        Epic retrieved = manager.getEpicById(1);
        assertEquals(epic, retrieved);
    }

    @Test
    void createEpic() {
        Epic epic = new Epic(1, "tasks.Epic 1", "Desc 1", Status.NEW);
        manager.createEpic(epic);

        assertEquals(1, manager.getAllEpics().size());
        assertEquals(epic, manager.getEpicById(1));
    }

    @Test
    void updateEpic() {
        Epic epic = new Epic(1, "tasks.Epic 1", "Desc 1", Status.NEW);
        manager.createEpic(epic);

        Epic updated = new Epic(1, "Updated tasks.Epic", "Updated desc", Status.DONE);
        manager.updateEpic(updated);

        Epic retrieved = manager.getEpicById(1);
        assertEquals(updated.getName(), retrieved.getName());
        assertEquals(updated.getDescription(), retrieved.getDescription());
        assertEquals(updated.getStatus(), retrieved.getStatus());
    }

    @Test
    void deleteEpicById() {
        Epic epic = new Epic(1, "tasks.Epic 1", "Desc 1", Status.NEW);
        manager.createEpic(epic);

        manager.deleteEpicById(1);
        assertTrue(manager.getAllEpics().isEmpty());
    }

    @Test
    void getAllSubtasks() {
        Epic epic = new Epic(1, "tasks.Epic 1", "Desc", Status.NEW);
        manager.createEpic(epic);

        Subtask subtask = new Subtask(2, "Sub 1", "Desc", Status.NEW, 1);
        manager.createSubtask(subtask);

        List<Subtask> subtasks = manager.getAllSubtasks();
        assertEquals(1, subtasks.size());
        assertEquals(subtask, subtasks.get(0));
    }

    @Test
    void deleteAllSubtasks() {
        Epic epic = new Epic(1, "tasks.Epic 1", "Desc", Status.NEW);
        manager.createEpic(epic);

        Subtask subtask = new Subtask(2, "Sub 1", "Desc", Status.NEW, 1);
        manager.createSubtask(subtask);

        manager.deleteAllSubtasks();
        assertTrue(manager.getAllSubtasks().isEmpty());
    }

    @Test
    void getSubtaskById() {
        Epic epic = new Epic(1, "tasks.Epic 1", "Desc", Status.NEW);
        manager.createEpic(epic);

        Subtask subtask = new Subtask(2, "Sub 1", "Desc", Status.NEW, 1);
        manager.createSubtask(subtask);

        Subtask retrieved = manager.getSubtaskById(2);
        assertEquals(subtask, retrieved);
    }

    @Test
    void createSubtask() {
        Epic epic = new Epic(1, "tasks.Epic 1", "Desc", Status.NEW);
        manager.createEpic(epic);

        Subtask subtask = new Subtask(2, "Sub 1", "Desc", Status.NEW, 1);
        manager.createSubtask(subtask);

        assertEquals(1, manager.getAllSubtasks().size());
        assertEquals(subtask, manager.getSubtaskById(2));
    }

    @Test
    void updateSubtask() {
        Epic epic = new Epic(1, "tasks.Epic 1", "Desc", Status.NEW);
        manager.createEpic(epic);

        Subtask subtask = new Subtask(2, "Sub 1", "Desc", Status.NEW, 1);
        manager.createSubtask(subtask);

        Subtask updated = new Subtask(2, "Updated Sub", "Updated desc", Status.DONE, 1);
        manager.updateSubtask(updated);

        Subtask retrieved = manager.getSubtaskById(2);
        assertEquals(updated.getName(), retrieved.getName());
        assertEquals(updated.getDescription(), retrieved.getDescription());
        assertEquals(updated.getStatus(), retrieved.getStatus());
    }

    @Test
    void deleteSubtaskById() {
        Epic epic = new Epic(1, "tasks.Epic 1", "Desc", Status.NEW);
        manager.createEpic(epic);

        Subtask subtask = new Subtask(2, "Sub 1", "Desc", Status.NEW, 1);
        manager.createSubtask(subtask);

        manager.deleteSubtaskById(2);
        assertTrue(manager.getAllSubtasks().isEmpty());
    }

    @Test
    void getSubtasksOfEpic() {
        Epic epic = new Epic(1, "tasks.Epic 1", "Desc", Status.NEW);
        manager.createEpic(epic);

        Subtask subtask1 = new Subtask(2, "Sub 1", "Desc", Status.NEW, 1);
        Subtask subtask2 = new Subtask(3, "Sub 2", "Desc", Status.NEW, 1);
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);

        List<Subtask> subs = manager.getSubtasksOfEpic(1);
        assertEquals(2, subs.size());
        assertTrue(subs.contains(subtask1));
        assertTrue(subs.contains(subtask2));
    }
}
