package server.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.google.gson.Gson;
import exceptions.NotFoundException;
import managers.ManagerSaveException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class BaseHttpHandler implements HttpHandler {

    protected final Gson gson = new Gson();

    protected void sendText(HttpExchange h, String text, int statusCode) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(statusCode, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        sendText(h, text, 200);
    }

    protected void sendNotFound(HttpExchange h) throws IOException {
        sendText(h, "{\"error\":\"Объект не найден\"}", 404);
    }

    protected void sendHasOverlaps(HttpExchange h) throws IOException {
        sendText(h, "{\"error\":\"Задача пересекается с существующими\"}", 406);
    }

    protected void sendCreated(HttpExchange h, String text) throws IOException {
        sendText(h, text, 201);
    }

    protected void sendInternalError(HttpExchange h) throws IOException {
        sendText(h, "{\"error\":\"Внутренняя ошибка сервера\"}", 500);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        sendText(exchange, "Method not implemented", 501);
    }

    protected String readRequestBody(HttpExchange exchange) throws IOException {
        return new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
    }

    protected void handleException(HttpExchange exchange, Exception e) throws IOException {
        e.printStackTrace();
        if (e instanceof NotFoundException) {
            sendNotFound(exchange);
        } else if (e instanceof ManagerSaveException && e.getMessage().contains("пересекается")) {
            sendHasOverlaps(exchange);
        } else {
            sendInternalError(exchange);
        }
    }

    protected void sendBadRequest(HttpExchange exchange, String message) throws IOException {
        sendText(exchange, "{\"error\":\"" + message + "\"}", 400);
    }
}