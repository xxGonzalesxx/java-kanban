package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.List;

/**
 * Интерфейс TaskManager для работы с задачами, эпиками и подзадачами.
 * 25.08.2025 сделано.
 */
public interface TaskManager {

    // Работа с Task
    List<Task> getAllTasks();

    List<Task> getHistory();

    void deleteAllTasks();

    Task getTaskById(int id);

    void createTask(Task task);

    void updateTask(Task task);

    void deleteTaskById(int id);

    // Работа с Epic
    List<Epic> getAllEpics();

    void deleteAllEpics();

    Epic getEpicById(int id);

    void createEpic(Epic epic);

    void updateEpic(Epic epic);

    void deleteEpicById(int id);

    // Работа с Subtask
    List<Subtask> getAllSubtasks();

    void deleteAllSubtasks();

    Subtask getSubtaskById(int id);

    void createSubtask(Subtask subtask);

    void updateSubtask(Subtask subtask);

    void deleteSubtaskById(int id);

    // Получаем подзадачи Epic
    List<Subtask> getSubtasksOfEpic(int epicId);
}
