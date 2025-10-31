package server.handler;

import com.sun.net.httpserver.HttpExchange;
import managers.TaskManager;
import tasks.Task;
import tasks.TaskDTO;
import tasks.TaskResponseDTO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TasksHandler extends BaseHttpHandler {
    private final TaskManager taskManager;

    public TasksHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();

            switch (method) {
                case "GET":
                    handleGet(exchange);
                    break;
                case "POST":
                    handlePost(exchange);
                    break;
                case "DELETE":
                    handleDelete(exchange);
                    break;
                default:
                    sendBadRequest(exchange, "Метод не поддерживается");
            }
        } catch (Exception e) {
            handleException(exchange, e);
        }
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String[] pathParts = path.split("/");

        if (pathParts.length == 2) {
            // GET /tasks - все задачи
            List<Task> tasks = taskManager.getAllTasks();
            // Конвертируем в DTO для сериализации
            List<TaskResponseDTO> responseDTOs = new ArrayList<>();
            for (Task task : tasks) {
                responseDTOs.add(new TaskResponseDTO(task));
            }
            String response = gson.toJson(responseDTOs);
            sendText(exchange, response);
        } else if (pathParts.length == 3) {
            // GET /tasks/{id} - задача по ID
            try {
                int id = Integer.parseInt(pathParts[2]);
                Task task = taskManager.getTaskById(id);
                if (task == null) {
                    sendNotFound(exchange);
                    return;
                }
                // Используем DTO для сериализации
                TaskResponseDTO responseDTO = new TaskResponseDTO(task);
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
            TaskDTO taskDTO = gson.fromJson(body, TaskDTO.class);
            Task task = new Task(taskDTO.id, taskDTO.name, taskDTO.description, taskDTO.status);

            if (task.getId() == 0) {
                taskManager.createTask(task);
                // Используем ResponseDTO для ответа
                TaskResponseDTO responseDTO = new TaskResponseDTO(task);
                String response = gson.toJson(responseDTO);
                sendCreated(exchange, response);
            } else {
                taskManager.updateTask(task);
                sendText(exchange, "{\"message\":\"Задача обновлена\"}");
            }
        } catch (Exception e) {
            handleException(exchange, e);
        }
    }

    private void handleDelete(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String[] pathParts = path.split("/");

        if (pathParts.length == 2) {
            // DELETE /tasks - все задачи
            taskManager.deleteAllTasks();
            sendText(exchange, "{\"message\":\"Все задачи удалены\"}");
        } else if (pathParts.length == 3) {
            // DELETE /tasks/{id} - задача по ID
            try {
                int id = Integer.parseInt(pathParts[2]);
                taskManager.deleteTaskById(id);
                sendText(exchange, "{\"message\":\"Задача удалена\"}");
            } catch (NumberFormatException e) {
                sendBadRequest(exchange, "Неверный формат ID");
            }
        }
    }
}