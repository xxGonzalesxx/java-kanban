public class Main {

    public static void main(String[] args) {
        System.out.println("Тестируем наше задание...");
        TaskManager taskManager= new InMemoryTaskManager();
        Task task1 = new Task(0,"Покупка продуктов","Купить колу и чипсов",Status.NEW);
        taskManager.createTask(task1);

        //Создаю Эпик
        Epic epic1 = new Epic(0,"Отправляемся на пляж","Берем очки и кепку",Status.NEW);
        taskManager.createEpic(epic1);

        //Создаю подзадачу
        Subtask subtask1 = new Subtask(0,"Собрать вещи","Взять сланцы",Status.NEW, epic1.getId());
        Subtask subtask2 = new Subtask(0,"Взять необходимые вещи","Пакет и деньши",Status.NEW, epic1.getId());
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        // Печатаем все задачи
        System.out.println("Все задачи: " + taskManager.getAllTasks());
        System.out.println("Все эпики: " +taskManager.getAllEpics());
        System.out.println("Все подзадачи: " + taskManager.getAllSubtasks());

        // Обновляем статус подзадачи
        subtask1.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask1);

        // Проверяем пересчёт статуса эпика
        System.out.println("Эпик после обновления подзадачи: " + taskManager.getEpicById(epic1.getId()));

        // Удаляем задачу
        taskManager.deleteTaskById(task1.getId());
        System.out.println("После удаления задачи: " +taskManager.getAllTasks());
    }

}

