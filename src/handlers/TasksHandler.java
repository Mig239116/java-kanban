package handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.sun.net.httpserver.HttpExchange;
import conversion.TaskListTypeToken;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import model.Task;
import model.TaskStatus;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TasksHandler extends BaseHttpHandler {

    public TasksHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    protected void handleGet(HttpExchange httpExchange) throws IOException {
        String[] pathParts = httpExchange.getRequestURI().getPath().split("/");
        if (pathParts.length == 2 & pathParts[1].equals("tasks")) {
            sendText(httpExchange, gson.toJson(taskManager.getAllTasks()));
        }

    }

    @Override
    protected void handlePost(HttpExchange httpExchange) throws IOException{
        String[] pathParts = httpExchange.getRequestURI().getPath().split("/");
        if (pathParts.length == 2 & pathParts[1].equals("tasks")) {
            List<Task> taskList = gson.fromJson(new String(httpExchange.getRequestBody().readAllBytes(),
                    StandardCharsets.UTF_8), new TaskListTypeToken().getType());
            for (Task task: taskList) {
                taskManager.createTask(task);
            }
        }
    }

    @Override
    protected void handleDelete(){

    }
}


