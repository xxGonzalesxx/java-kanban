import managers.FileBackedTaskManager;
import managers.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== ТЕСТИРОВАНИЕ НОВОЙ ФУНКЦИОНАЛЬНОСТИ 8 СПРИНТА ===");

        File file = new File("tasks.csv");
        TaskManager taskManager = FileBackedTaskManager.loadFromFile(file);

        // 1. ТЕСТ: Создание задач с временем
        System.out.println("\n1. Тест создания задач с временем:");
        Task task1 = new Task(0, "Покупка продуктов", "Купить колу и чипсов", Status.NEW,
                Duration.ofMinutes(30), LocalDateTime.of(2024, 10, 1, 10, 0));
        Task task2 = new Task(0, "Уборка", "Помыть полы", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2024, 10, 1, 9, 0));

        taskManager.createTask(task1);
        taskManager.createTask(task2);
        System.out.println("✅ Задачи созданы");

        // 2. ТЕСТ: getPrioritizedTasks()
        System.out.println("\n2. Тест getPrioritizedTasks():");
        System.out.println("Отсортированные задачи:");
        for (Task task : taskManager.getPrioritizedTasks()) {
            System.out.println("  " + task.getName() + " - " + task.getStartTime() +
                    " (длительность: " + task.getDuration() + ")");
        }

        // 3. ТЕСТ: Создание Epic с подзадачами (время Epic)
        System.out.println("\n3. Тест времени Epic:");
        Epic epic1 = new Epic(0, "Отправляемся на пляж", "Берем очки и кепку", Status.NEW, null, null);
        taskManager.createEpic(epic1);

        Subtask subtask1 = new Subtask(0, "Собрать вещи", "Взять сланцы", Status.NEW, epic1.getId(),
                Duration.ofMinutes(15), LocalDateTime.of(2024, 10, 1, 11, 0));
        Subtask subtask2 = new Subtask(0, "Взять необходимые вещи", "Пакет и деньги", Status.NEW, epic1.getId(),
                Duration.ofMinutes(10), LocalDateTime.of(2024, 10, 1, 8, 30));

        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        // Проверяем время Epic
        Epic updatedEpic = taskManager.getEpicById(epic1.getId());
        System.out.println("Время Epic: startTime=" + updatedEpic.getStartTime() +
                ", endTime=" + updatedEpic.getEndTime());

        // 4. ТЕСТ: Проверка пересечений
        System.out.println("\n4. Тест проверки пересечений:");
        try {
            Task conflictTask = new Task(0, "Конфликтная задача", "Пересекается по времени", Status.NEW,
                    Duration.ofMinutes(30), LocalDateTime.of(2024, 10, 1, 9, 30)); // пересекается с "Уборка"
            taskManager.createTask(conflictTask);
            System.out.println("❌ Ошибка: пересечение не обнаружено!");
        } catch (Exception e) {
            System.out.println("✅ Пересечение корректно обнаружено: " + e.getMessage());
        }

        // 5. ТЕСТ: Задачи без времени (не должны влиять на сортировку)
        System.out.println("\n5. Тест задач без времени:");
        Task taskWithoutTime = new Task(0, "Задача без времени", "Нет времени", Status.NEW);
        taskManager.createTask(taskWithoutTime);
        System.out.println("✅ Задача без времени создана (не влияет на сортировку)");

        // Финальный вывод
        System.out.println("\n=== ФИНАЛЬНЫЙ ОБЗОР ===");
        printAll(taskManager);

        System.out.println("\n=== ОТСОРТИРОВАННЫЕ ЗАДАЧИ ===");
        for (Task task : taskManager.getPrioritizedTasks()) {
            System.out.println("  " + task.getName() + " - " + task.getStartTime());
        }
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