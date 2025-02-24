package handlers;

import com.sun.net.httpserver.HttpExchange;
import exceptions.IntersectionException;
import exceptions.NotFoundException;
import manager.TaskManager;
import model.Epic;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class EpicsHandler extends BaseHttpHandler {

    public EpicsHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    protected void handleGet(HttpExchange httpExchange) throws IOException {
        String[] pathParts = httpExchange.getRequestURI().getPath().split("/");
        if (pathParts.length == 2 & pathParts[1].equals("epics")) {
            sendText(httpExchange, gson.toJson(taskManager.getAllEpics()), 200);
        }
        try {
            if (pathParts.length == 3 & pathParts[1].equals("epics")) {
                if (getEntryId(httpExchange).isEmpty()) {
                    sendText(httpExchange,"Некорректный идентификатор поста", 400);
                    return;
                }
                sendText(httpExchange,
                        gson.toJson(taskManager.getEpicById(this.getEntryId(httpExchange).get())),
                        200);
            }

            if (pathParts.length == 4 & pathParts[1].equals("epics")) {
                if (getEntryId(httpExchange).isEmpty()) {
                    sendText(httpExchange,"Некорректный идентификатор поста", 400);
                    return;
                }
                sendText(httpExchange,
                        gson.toJson(taskManager.getEpicsSubtasks(this.getEntryId(httpExchange).get())), 200);
            }
        } catch (NotFoundException e) {
            sendText(httpExchange, e.getMessage(), 404);
        }
    }

    @Override
    protected void handlePost(HttpExchange httpExchange) throws IOException {
        String[] pathParts = httpExchange.getRequestURI().getPath().split("/");
        try {
            if (pathParts.length == 2 & pathParts[1].equals("epics")) {
                Epic epic = gson.fromJson(new String(httpExchange.getRequestBody().readAllBytes(),
                        StandardCharsets.UTF_8), Epic.class);
                taskManager.createEpic(epic);
                String message = "{\"taskID\": " + epic.getTaskID() + ",\"message\": \"Эпик создан\"}";
                sendText(httpExchange, message, 201);
            }
            if (pathParts.length == 3 & pathParts[1].equals("epics")) {
                if (getEntryId(httpExchange).isEmpty()) {
                    sendText(httpExchange,"Некорректный идентификатор поста", 400);
                    return;
                }
                Epic epic = gson.fromJson(new String(httpExchange.getRequestBody().readAllBytes(),
                        StandardCharsets.UTF_8), Epic.class);
                epic.setTaskID(this.getEntryId(httpExchange).get());
                taskManager.updateEpic(epic);
                sendText(httpExchange, "Эпик обновлен", 201);
            }
        } catch (NotFoundException e) {
            sendText(httpExchange, e.getMessage(), 404);
        } catch (IntersectionException e) {
            sendText(httpExchange, e.getMessage(), 406);
        }
    }

    @Override
    protected void handleDelete(HttpExchange httpExchange) throws IOException {
        String[] pathParts = httpExchange.getRequestURI().getPath().split("/");
        try {
            if (pathParts.length == 3 & pathParts[1].equals("epics")) {
                taskManager.deleteEpicByID(this.getEntryId(httpExchange).get());
                sendText(httpExchange, "Эпик удален", 200);
            }
        } catch (NotFoundException e) {
            sendText(httpExchange, e.getMessage(), 404);
        }
    }
}
