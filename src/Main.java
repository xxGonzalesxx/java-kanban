public class Main {
    public static void main(String[] args) {
        System.out.println("Тестируем наше задание...");
        TaskManager taskManager= new InMemoryTaskManager();

        Task task1 = new Task(0,"Покупка продуктов","Купить колу и чипсов",Status.NEW);
        taskManager.createTask(task1);

        Epic epic1 = new Epic(0,"Отправляемся на пляж","Берем очки и кепку",Status.NEW);
        taskManager.createEpic(epic1);

        Subtask subtask1 = new Subtask(0,"Собрать вещи","Взять сланцы",Status.NEW, epic1.getId());
        Subtask subtask2 = new Subtask(0,"Взять необходимые вещи","Пакет и деньши",Status.NEW, epic1.getId());
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        System.out.println("Все задачи: " + taskManager.getAllTasks());
        System.out.println("Все эпики: " +taskManager.getAllEpics());
        System.out.println("Все подзадачи: " + taskManager.getAllSubtasks());

        // --- Тестируем историю ---
        taskManager.getTaskById(task1.getId());    // просмотрели task1
        taskManager.getEpicById(epic1.getId());    // просмотрели epic1
        taskManager.getSubtaskById(subtask1.getId()); // просмотрели subtask1
        taskManager.getTaskById(task1.getId());    // повторный просмотр task1

        System.out.println("История просмотров: " + taskManager.getHistory());

        // Обновляем статус подзадачи
        subtask1.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask1);
        System.out.println("Эпик после обновления подзадачи: " + taskManager.getEpicById(epic1.getId()));

        // Удаляем задачу
        taskManager.deleteTaskById(task1.getId());
        System.out.println("После удаления задачи: " +taskManager.getAllTasks());

        // Проверяем историю после удаления
        System.out.println("История после удаления task1: " + taskManager.getHistory());
    }
}
