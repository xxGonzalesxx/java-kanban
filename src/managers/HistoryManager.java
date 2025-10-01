package managers;

import tasks.Task;
import java.util.List;

public interface HistoryManager {

    // Добавляем задачу в историю
    void add(Task task);

    // Получаем историю задач
    List<Task> getHistory();
}
