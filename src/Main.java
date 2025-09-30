import managers.FileBackedTaskManager;
import managers.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        System.out.println("Фаза 1: Создание задач и сохранение");

        File file = new File("tasks.csv");
        TaskManager taskManager = FileBackedTaskManager.loadFromFile(file);

        // Создаём обычную задачу
        Task task1 = new Task(0, "Покупка продуктов", "Купить колу и чипсов", Status.NEW);
        taskManager.createTask(task1);

        // Создаём эпик
        Epic epic1 = new Epic(0, "Отправляемся на пляж", "Берем очки и кепку", Status.NEW);
        taskManager.createEpic(epic1);

        // Создаём подзадачи для эпика
        Subtask subtask1 = new Subtask(0, "Собрать вещи", "Взять сланцы", Status.NEW, epic1.getId());
        Subtask subtask2 = new Subtask(0, "Взять необходимые вещи", "Пакет и деньги", Status.NEW, epic1.getId());
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        // Печатаем все задачи
        printAll(taskManager);

        // Обновляем статус подзадачи
        subtask1.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask1);

        // Проверяем пересчёт статуса эпика
        System.out.println("Эпик после обновления подзадачи:");
        System.out.println("  " + taskManager.getEpicById(epic1.getId()));

        // Удаляем задачу
        taskManager.deleteTaskById(task1.getId());
        System.out.println("После удаления задачи:");
        printAll(taskManager);

        System.out.println("\nФаза 2: Загрузка менеджера из файла");
        TaskManager loadedManager = FileBackedTaskManager.loadFromFile(file);
        printAll(loadedManager);
    }

    private static void printAll(TaskManager manager) {
        System.out.println("Все задачи:");
        if (manager.getAllTasks().isEmpty()) System.out.println("  Нет задач");
        else manager.getAllTasks().forEach(task -> System.out.println("  " + task));

        System.out.println("Все эпики:");
        if (manager.getAllEpics().isEmpty()) System.out.println("  Нет эпиков");
        else manager.getAllEpics().forEach(epic -> System.out.println("  " + epic));

        System.out.println("Все подзадачи:");
        if (manager.getAllSubtasks().isEmpty()) System.out.println("  Нет подзадач");
        else manager.getAllSubtasks().forEach(subtask -> System.out.println("  " + subtask));
    }
}
