package handlers;

import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;

import java.io.IOException;

public class PrioritizedHandler extends BaseHttpHandler {

    public PrioritizedHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    protected void handleGet(HttpExchange httpExchange) throws IOException {
        String[] pathParts = httpExchange.getRequestURI().getPath().split("/");
        if (pathParts.length == 2 & pathParts[1].equals("prioritized")) {
            sendText(httpExchange,
                    gson.toJson(taskManager.getPrioritizedTasks()),
                    200);
        }
    }

    @Override
    protected void handlePost(HttpExchange httpExchange) throws IOException {
    }

    @Override
    protected void handleDelete(HttpExchange httpExchange) throws IOException {
    }
}
