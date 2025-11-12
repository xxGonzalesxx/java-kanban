package server.handler;

import com.sun.net.httpserver.HttpExchange;
import managers.TaskManager;
import tasks.Task;
import tasks.TaskResponseDTO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PrioritizedHandler extends BaseHttpHandler {
    private final TaskManager taskManager;

    public PrioritizedHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if ("GET".equals(exchange.getRequestMethod())) {
                List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
                // Конвертируем в DTO
                List<TaskResponseDTO> responseDTOs = new ArrayList<>();
                for (Task task : prioritizedTasks) {
                    responseDTOs.add(new TaskResponseDTO(task));
                }
                String response = gson.toJson(responseDTOs);
                sendText(exchange, response);
            } else {
                sendBadRequest(exchange, "Метод не поддерживается");
            }
        } catch (Exception e) {
            handleException(exchange, e);
        }
    }
}