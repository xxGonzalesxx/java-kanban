package server.handler;

import com.google.gson.Gson;
import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.HttpTaskServer;
import tasks.Epic;
import tasks.Subtask;
import tasks.SubtaskDTO;
import tasks.SubtaskResponseDTO;
import tasks.Status;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerSubtasksTest {
    private TaskManager manager;
    private HttpTaskServer taskServer;
    private Gson gson;
    private final HttpClient client = HttpClient.newHttpClient();

    @BeforeEach
    void setUp() throws IOException {
        manager = Managers.getDefault();
        taskServer = new HttpTaskServer(manager);
        gson = new Gson();
        taskServer.start();
    }

    @AfterEach
    void tearDown() {
        taskServer.stop();
    }

    @Test
    void testAddSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic(0, "Parent Epic", "For subtask", Status.NEW);
        manager.createEpic(epic);

        // Используем DTO вместо прямого Subtask
        SubtaskDTO subtaskDTO = new SubtaskDTO();
        subtaskDTO.id = 0;
        subtaskDTO.name = "Test Subtask";
        subtaskDTO.description = "Testing subtask";
        subtaskDTO.status = Status.NEW;
        subtaskDTO.epicId = epic.getId();

        String subtaskJson = gson.toJson(subtaskDTO);

        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        List<Subtask> subtasksFromManager = manager.getAllSubtasks();
        assertEquals(1, subtasksFromManager.size());
        assertEquals("Test Subtask", subtasksFromManager.get(0).getName());
    }

    @Test
    void testGetSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic(0, "Parent Epic", "Desc", Status.NEW);
        manager.createEpic(epic);
        Subtask subtask = new Subtask(0, "Test Subtask", "Description", Status.NEW, epic.getId());
        manager.createSubtask(subtask);

        URI url = URI.create("http://localhost:8080/subtasks/" + subtask.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        // Используем ResponseDTO для десериализации
        SubtaskResponseDTO responseDTO = gson.fromJson(response.body(), SubtaskResponseDTO.class);
        assertEquals(subtask.getId(), responseDTO.id);
        assertEquals(subtask.getName(), responseDTO.name);
        assertEquals(subtask.getEpicId(), responseDTO.epicId);
    }

    @Test
    void testGetAllSubtasks() throws IOException, InterruptedException {
        Epic epic = new Epic(0, "Parent Epic", "Desc", Status.NEW);
        manager.createEpic(epic);

        Subtask subtask1 = new Subtask(0, "Subtask 1", "Desc 1", Status.NEW, epic.getId());
        Subtask subtask2 = new Subtask(0, "Subtask 2", "Desc 2", Status.NEW, epic.getId());
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);

        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        // Используем ResponseDTO массив для десериализации
        SubtaskResponseDTO[] subtasks = gson.fromJson(response.body(), SubtaskResponseDTO[].class);
        assertEquals(2, subtasks.length);
    }
}