package server.handler;

import com.sun.net.httpserver.HttpExchange;
import managers.TaskManager;
import tasks.Subtask;
import tasks.SubtaskDTO;
import tasks.SubtaskResponseDTO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SubtasksHandler extends BaseHttpHandler {
    private final TaskManager taskManager;

    public SubtasksHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();

            switch (method) {
                case "GET":
                    handleGet(exchange, path);
                    break;
                case "POST":
                    handlePost(exchange);
                    break;
                case "DELETE":
                    handleDelete(exchange, path);
                    break;
                default:
                    sendBadRequest(exchange, "Метод не поддерживается");
            }
        } catch (Exception e) {
            handleException(exchange, e);
        }
    }

    private void handleGet(HttpExchange exchange, String path) throws IOException {
        String[] pathParts = path.split("/");

        if (pathParts.length == 2) {
            // GET /subtasks - все подзадачи
            List<Subtask> subtasks = taskManager.getAllSubtasks();
            List<SubtaskResponseDTO> responseDTOs = new ArrayList<>();
            for (Subtask subtask : subtasks) {
                responseDTOs.add(new SubtaskResponseDTO(subtask));
            }
            String response = gson.toJson(responseDTOs);
            sendText(exchange, response);
        } else if (pathParts.length == 3) {
            // GET /subtasks/{id} - подзадача по ID
            try {
                int id = Integer.parseInt(pathParts[2]);
                Subtask subtask = taskManager.getSubtaskById(id);
                if (subtask == null) {
                    sendNotFound(exchange);
                    return;
                }
                SubtaskResponseDTO responseDTO = new SubtaskResponseDTO(subtask);
                String response = gson.toJson(responseDTO);
                sendText(exchange, response);
            } catch (NumberFormatException e) {
                sendBadRequest(exchange, "Неверный формат ID");
            }
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        String body = readRequestBody(exchange);

        try {
            SubtaskDTO subtaskDTO = gson.fromJson(body, SubtaskDTO.class);
            // Используем конструктор с epicId
            Subtask subtask = new Subtask(
                    subtaskDTO.id,
                    subtaskDTO.name,
                    subtaskDTO.description,
                    subtaskDTO.status,
                    subtaskDTO.epicId
            );

            if (subtask.getId() == 0) {
                taskManager.createSubtask(subtask);
                SubtaskResponseDTO responseDTO = new SubtaskResponseDTO(subtask);
                String response = gson.toJson(responseDTO);
                sendCreated(exchange, response);
            } else {
                taskManager.updateSubtask(subtask);
                sendText(exchange, "{\"message\":\"Подзадача обновлена\"}");
            }
        } catch (Exception e) {
            handleException(exchange, e);
        }
    }

    private void handleDelete(HttpExchange exchange, String path) throws IOException {
        String[] pathParts = path.split("/");

        if (pathParts.length == 2) {
            // DELETE /subtasks - все подзадачи
            taskManager.deleteAllSubtasks();
            sendText(exchange, "{\"message\":\"Все подзадачи удалены\"}");
        } else if (pathParts.length == 3) {
            // DELETE /subtasks/{id} - подзадача по ID
            try {
                int id = Integer.parseInt(pathParts[2]);
                taskManager.deleteSubtaskById(id);
                sendText(exchange, "{\"message\":\"Подзадача удалена\"}");
            } catch (NumberFormatException e) {
                sendBadRequest(exchange, "Неверный формат ID");
            }
        }
    }
}