package handlers;

import com.sun.net.httpserver.HttpExchange;
import exceptions.IntersectionException;
import exceptions.NotFoundException;
import manager.TaskManager;
import model.Subtask;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class SubtasksHandler extends BaseHttpHandler {

    public SubtasksHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    protected void handleGet(HttpExchange httpExchange) throws IOException {
        String[] pathParts = httpExchange.getRequestURI().getPath().split("/");
        if (pathParts.length == 2 & pathParts[1].equals("subtasks")) {
            sendText(httpExchange, gson.toJson(taskManager.getAllSubtasks()), 200);
        }
        try{
            if (pathParts.length == 3 & pathParts[1].equals("subtasks")) {
                if (getEntryId(httpExchange).isEmpty()) {
                    sendText(httpExchange,"Некорректный идентификатор поста", 400);
                    return;
                }
                sendText(httpExchange,
                        gson.toJson(taskManager.getSubtaskById(this.getEntryId(httpExchange).get())),
                        200);
            }
        } catch (NotFoundException e) {
                sendText(httpExchange, e.getMessage(), 404);
        }
    }

    @Override
    protected void handlePost(HttpExchange httpExchange) throws IOException{
        String[] pathParts = httpExchange.getRequestURI().getPath().split("/");
        try{
            if (pathParts.length == 2 & pathParts[1].equals("subtasks")) {
                Subtask subtask = gson.fromJson(new String(httpExchange.getRequestBody().readAllBytes(),
                        StandardCharsets.UTF_8), Subtask.class);

                taskManager.createSubtask(subtask);
                sendText(httpExchange, "Подзадача создана", 201);
            }
            if (pathParts.length == 3 & pathParts[1].equals("subtasks")) {
                if (getEntryId(httpExchange).isEmpty()) {
                    sendText(httpExchange,"Некорректный идентификатор поста", 400);
                    return;
                }
                Subtask subtask = gson.fromJson(new String(httpExchange.getRequestBody().readAllBytes(),
                        StandardCharsets.UTF_8), Subtask.class);
                subtask.setTaskID(this.getEntryId(httpExchange).get());
                taskManager.updateSubtask(subtask);
                sendText(httpExchange, "Подзадача обновлена", 201);
            }
        } catch (NotFoundException e) {
            sendText(httpExchange, e.getMessage(), 404);
        } catch (IntersectionException e) {
            sendText(httpExchange, e.getMessage(), 406);
        }

    }

    @Override
    protected void handleDelete(HttpExchange httpExchange) throws IOException{
        String[] pathParts = httpExchange.getRequestURI().getPath().split("/");
        try {
            if (pathParts.length == 3 & pathParts[1].equals("subtasks")) {
                if (getEntryId(httpExchange).isEmpty()) {
                    sendText(httpExchange,"Некорректный идентификатор поста", 400);
                    return;
                }
                taskManager.deleteSubtaskByID(this.getEntryId(httpExchange).get());
                sendText(httpExchange, "Подзадача удалена", 200);
            }
        }
        catch (NotFoundException e) {
            sendText(httpExchange, e.getMessage(), 404);
        }
    }
}
