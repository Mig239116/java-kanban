package handlers;

import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import model.Task;

import java.io.IOException;
import java.util.List;

public class HistoryHandler extends BaseHttpHandler {

    public HistoryHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    protected void handleGet(HttpExchange httpExchange) throws IOException {
        String[] pathParts = httpExchange.getRequestURI().getPath().split("/");
        if (pathParts.length == 2 & pathParts[1].equals("history")) {
            sendText(httpExchange, gson.toJson(taskManager.getHistory()), 200);
        }
    }

    @Override
    protected void handlePost(HttpExchange httpExchange) throws IOException {
    }

    @Override
    protected void handleDelete(HttpExchange httpExchange) throws IOException {
    }
}
