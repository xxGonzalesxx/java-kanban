package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.List;
// 25.08.2025 сделал interface managers.TaskManager
public interface TaskManager {
    // tasks.Task
    List<Task> getAllTasks();

    List<Task> getHistory();

    void deleteAllTasks();

    Task getTaskById(int id);

    void createTask(Task task);

    void updateTask(Task task);

    void deleteTaskById(int id);

    // Работа с tasks.Epic
    List<Epic> getAllEpics();

    void deleteAllEpics();

    Epic getEpicById(int id);

    void createEpic(Epic epic);

    void updateEpic(Epic epic);

    void deleteEpicById(int id);

    //  Работа с tasks.Subtask
    List<Subtask> getAllSubtasks();

    void deleteAllSubtasks();

    Subtask getSubtaskById(int id);

    void createSubtask(Subtask subtask);

    void updateSubtask(Subtask subtask);

    void deleteSubtaskById(int id);



    // получаем подзадачи tasks.Epic
    List<Subtask> getSubtasksOfEpic(int epicId);
}
