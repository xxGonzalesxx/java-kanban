package server;

import com.sun.net.httpserver.HttpServer;
import managers.Managers;
import managers.TaskManager;
import server.handler.*;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080; // ← убрать пробел
    private final HttpServer server;
    private final TaskManager taskManager;

    public HttpTaskServer(TaskManager taskManager) throws IOException { // ← убрать пробел перед (
        this.taskManager = taskManager;
        this.server = HttpServer.create(new InetSocketAddress(PORT), 0);
        configureEndpoints();
    }

    // Настройка эндпоинтов
    private void configureEndpoints() {
        server.createContext("/tasks", new TasksHandler(taskManager));
        server.createContext("/subtasks", new SubtasksHandler(taskManager));
        server.createContext("/epics", new EpicsHandler(taskManager));
        server.createContext("/history", new HistoryHandler(taskManager));
        server.createContext("/prioritized", new PrioritizedHandler(taskManager));
    }

    public void start() {
        server.start();
        System.out.println("Сервер запущен на порту " + PORT);
    }

    public void stop() {
        server.stop(0);
        System.out.println("HTTP сервер остановлен.");
    }

    public static void main(String[] args) throws IOException {
        TaskManager manager = Managers.getDefault();
        HttpTaskServer taskServer = new HttpTaskServer(manager);
        taskServer.start();

        System.out.println("Сервер запущен на http://localhost:8080");
        System.out.println("Для остановки нажмите Ctrl+C");
    }
}