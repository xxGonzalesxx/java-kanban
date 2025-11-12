package server.handler;

import com.google.gson.Gson;
import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.HttpTaskServer;
import tasks.Epic;
import tasks.EpicDTO;
import tasks.EpicResponseDTO;
import tasks.Status;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerEpicsTest {
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
    void testAddEpic() throws IOException, InterruptedException {
        // Используем DTO вместо прямого Epic
        EpicDTO epicDTO = new EpicDTO();
        epicDTO.id = 0;
        epicDTO.name = "Test Epic";
        epicDTO.description = "Testing epic";
        epicDTO.status = Status.NEW;

        String epicJson = gson.toJson(epicDTO);

        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        List<Epic> epicsFromManager = manager.getAllEpics();
        assertEquals(1, epicsFromManager.size());
        assertEquals("Test Epic", epicsFromManager.get(0).getName());
    }

    @Test
    void testGetEpic() throws IOException, InterruptedException {
        Epic epic = new Epic(0, "Test Epic", "Description", Status.NEW);
        manager.createEpic(epic);

        URI url = URI.create("http://localhost:8080/epics/" + epic.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        // Используем ResponseDTO для десериализации
        EpicResponseDTO responseDTO = gson.fromJson(response.body(), EpicResponseDTO.class);
        assertEquals(epic.getId(), responseDTO.id);
        assertEquals(epic.getName(), responseDTO.name);
    }

    @Test
    void testGetAllEpics() throws IOException, InterruptedException {
        Epic epic1 = new Epic(0, "Epic 1", "Desc 1", Status.NEW);
        Epic epic2 = new Epic(0, "Epic 2", "Desc 2", Status.NEW);
        manager.createEpic(epic1);
        manager.createEpic(epic2);

        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        // Используем ResponseDTO массив для десериализации
        EpicResponseDTO[] epics = gson.fromJson(response.body(), EpicResponseDTO[].class);
        assertEquals(2, epics.length);
    }

    @Test
    void testDeleteEpic() throws IOException, InterruptedException {
        Epic epic = new Epic(0, "To Delete", "Desc", Status.NEW);
        manager.createEpic(epic);

        URI url = URI.create("http://localhost:8080/epics/" + epic.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        assertTrue(manager.getAllEpics().isEmpty());
    }
}