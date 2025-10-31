package server.handler;

import com.sun.net.httpserver.HttpExchange;
import managers.TaskManager;
import tasks.Epic;
import tasks.EpicDTO;
import tasks.EpicResponseDTO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EpicsHandler extends BaseHttpHandler {
    private final TaskManager taskManager;

    public EpicsHandler(TaskManager taskManager) {
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
            // GET /epics - все эпики
            List<Epic> epics = taskManager.getAllEpics();
            List<EpicResponseDTO> responseDTOs = new ArrayList<>();
            for (Epic epic : epics) {
                responseDTOs.add(new EpicResponseDTO(epic));
            }
            String response = gson.toJson(responseDTOs);
            sendText(exchange, response);
        } else if (pathParts.length == 3) {
            // GET /epics/{id} - эпик по ID
            try {
                int id = Integer.parseInt(pathParts[2]);
                Epic epic = taskManager.getEpicById(id);
                if (epic == null) {
                    sendNotFound(exchange);
                    return;
                }
                EpicResponseDTO responseDTO = new EpicResponseDTO(epic);
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
            EpicDTO epicDTO = gson.fromJson(body, EpicDTO.class);
            Epic epic = new Epic(epicDTO.id, epicDTO.name, epicDTO.description, epicDTO.status);

            if (epic.getId() == 0) {
                taskManager.createEpic(epic);
                EpicResponseDTO responseDTO = new EpicResponseDTO(epic);
                String response = gson.toJson(responseDTO);
                sendCreated(exchange, response);
            } else {
                taskManager.updateEpic(epic);
                sendText(exchange, "{\"message\":\"Эпик обновлен\"}");
            }
        } catch (Exception e) {
            handleException(exchange, e);
        }
    }

    private void handleDelete(HttpExchange exchange, String path) throws IOException {
        String[] pathParts = path.split("/");

        if (pathParts.length == 2) {
            // DELETE /epics - все эпики
            taskManager.deleteAllEpics();
            sendText(exchange, "{\"message\":\"Все эпики удалены\"}");
        } else if (pathParts.length == 3) {
            // DELETE /epics/{id} - эпик по ID
            try {
                int id = Integer.parseInt(pathParts[2]);
                taskManager.deleteEpicById(id);
                sendText(exchange, "{\"message\":\"Эпик удален\"}");
            } catch (NumberFormatException e) {
                sendBadRequest(exchange, "Неверный формат ID");
            }
        }
    }
}