package server.handler;

import com.google.gson.Gson;
import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.HttpTaskServer;
import tasks.Task;
import tasks.TaskResponseDTO;
import tasks.Status;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerHistoryTest {
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
    void testGetHistory() throws IOException, InterruptedException {
        // Создаем задачи и добавляем их в историю
        Task task1 = new Task(0, "Task 1", "Desc 1", Status.NEW);
        Task task2 = new Task(0, "Task 2", "Desc 2", Status.NEW);
        manager.createTask(task1);
        manager.createTask(task2);

        // Добавляем в историю через GET запросы
        manager.getTaskById(task1.getId());
        manager.getTaskById(task2.getId());

        URI url = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        // Используем ResponseDTO массив для десериализации
        TaskResponseDTO[] history = gson.fromJson(response.body(), TaskResponseDTO[].class);
        assertEquals(2, history.length);
    }

    @Test
    void testGetEmptyHistory() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        // Используем ResponseDTO массив для десериализации
        TaskResponseDTO[] history = gson.fromJson(response.body(), TaskResponseDTO[].class);
        assertEquals(0, history.length);
    }
}