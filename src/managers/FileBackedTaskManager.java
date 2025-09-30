package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.Status;

import java.io.*;
import java.util.*;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    // Сохраняем все задачи в CSV
    protected void save() {
        try (Writer writer = new FileWriter(file)) {
            writer.write("id,type,name,status,description,epic\n"); // заголовок CSV

            for (Task task : getAllTasks()) {
                writer.write(toString(task) + "\n");
            }
            for (Epic epic : getAllEpics()) {
                writer.write(toString(epic) + "\n");
            }
            for (Subtask subtask : getAllSubtasks()) {
                writer.write(toString(subtask) + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении данных", e);
        }
    }

    // Преобразуем задачу в строку CSV
    private String toString(Task task) {
        return String.format("%d,%s,%s,%s,%s,%s",
                task.getId(),
                task.getType(),
                task.getName(),
                task.getStatus(),
                task.getDescription(),
                task instanceof Subtask ? ((Subtask) task).getEpicId() : "");
    }

    // Преобразуем строку CSV обратно в объект задачи
    private Task fromString(String value) {
        String[] fields = value.split(",");
        int id = Integer.parseInt(fields[0]);
        String type = fields[1];
        String name = fields[2];
        Status status = Status.valueOf(fields[3]);
        String description = fields[4];

        return switch (type) {
            case "TASK" -> new Task(id, name, description, status);
            case "EPIC" -> new Epic(id, name, description, status);
            case "SUBTASK" -> new Subtask(id, name, description, status, Integer.parseInt(fields[5]));
            default -> throw new IllegalArgumentException("Неизвестный тип задачи: " + type);
        };
    }

    // Переопределяем методы для автосохранения
    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    // Загружаем менеджер из файла
    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        if (!file.exists()) return manager;

        Map<Integer, Subtask> subtaskMap = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.readLine(); // пропускаем заголовок
            String line;
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                Task task = manager.fromString(line);
                if (task instanceof Epic epic) {
                    manager.epics.put(epic.getId(), epic);
                    if (manager.nextId <= epic.getId()) manager.nextId = epic.getId() + 1;
                } else if (task instanceof Task t && !(task instanceof Subtask)) {
                    manager.tasks.put(t.getId(), t);
                    if (manager.nextId <= t.getId()) manager.nextId = t.getId() + 1;
                } else if (task instanceof Subtask subtask) {
                    subtaskMap.put(subtask.getId(), subtask);
                    if (manager.nextId <= subtask.getId()) manager.nextId = subtask.getId() + 1;
                }
            }

            // Создаем подзадачи и связываем с эпиками
            for (Subtask subtask : subtaskMap.values()) {
                manager.subtasks.put(subtask.getId(), subtask);
                Epic epic = manager.epics.get(subtask.getEpicId());
                if (epic != null) {
                    epic.addSubtaskId(subtask.getId());
                    manager.updateEpicStatus(epic);
                }
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при загрузке данных", e);
        }

        return manager;
    }
}
