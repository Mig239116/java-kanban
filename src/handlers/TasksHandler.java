package handlers;

import com.sun.net.httpserver.HttpExchange;
import exceptions.IntersectionException;
import exceptions.NotFoundException;
import manager.TaskManager;
import model.Task;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TasksHandler extends BaseHttpHandler {

    public TasksHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    protected void handleGet(HttpExchange httpExchange) throws IOException {
        String[] pathParts = httpExchange.getRequestURI().getPath().split("/");
        if (pathParts.length == 2 & pathParts[1].equals("tasks")) {
            sendText(httpExchange, gson.toJson(taskManager.getAllTasks()), 200);
        }
        try{
            if (pathParts.length == 3 & pathParts[1].equals("tasks")) {
                if (getEntryId(httpExchange).isEmpty()) {
                    sendText(httpExchange,"Некорректный идентификатор поста", 400);
                    return;
                }
                sendText(httpExchange,
                        gson.toJson(taskManager.getTaskById(this.getEntryId(httpExchange).get())),
                        200);
            }
        } catch (NotFoundException e) {
            sendText(httpExchange, e.getMessage(), 404);
        }

    }

    @Override
    protected void handlePost(HttpExchange httpExchange) throws IOException{
        String[] pathParts = httpExchange.getRequestURI().getPath().split("/");
        try {
            if (pathParts.length == 2 & pathParts[1].equals("tasks")) {
                Task task = gson.fromJson(new String(httpExchange.getRequestBody().readAllBytes(),
                        StandardCharsets.UTF_8), Task.class);

                taskManager.createTask(task);
                sendText(httpExchange, "Задача создана", 201);
            }
            if (pathParts.length == 3 & pathParts[1].equals("tasks")) {
                if (getEntryId(httpExchange).isEmpty()) {
                    sendText(httpExchange,"Некорректный идентификатор поста", 400);
                    return;
                }
                Task task = gson.fromJson(new String(httpExchange.getRequestBody().readAllBytes(),
                        StandardCharsets.UTF_8), Task.class);
                task.setTaskID(this.getEntryId(httpExchange).get());
                taskManager.updateTask(task);
                sendText(httpExchange, "Задача обновлена", 201);
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
        try{
            if (pathParts.length == 3 & pathParts[1].equals("tasks")) {
                if (getEntryId(httpExchange).isEmpty()) {
                    sendText(httpExchange,"Некорректный идентификатор поста", 400);
                    return;
                }
                taskManager.deleteTaskByID(this.getEntryId(httpExchange).get());
                sendText(httpExchange, "Задача удалена", 200);
            }
        } catch (NotFoundException e) {
            sendText(httpExchange, e.getMessage(), 404);
        }
    }
}


