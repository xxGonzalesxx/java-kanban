package server.handler;

import com.sun.net.httpserver.HttpExchange;
import managers.TaskManager;
import tasks.Task;
import tasks.TaskResponseDTO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HistoryHandler extends BaseHttpHandler {
    private final TaskManager taskManager;

    public HistoryHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if ("GET".equals(exchange.getRequestMethod())) {
                List<Task> history = taskManager.getHistory();
                // Конвертируем в DTO
                List<TaskResponseDTO> responseDTOs = new ArrayList<>();
                for (Task task : history) {
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