import com.sun.net.httpserver.HttpServer;

import handlers.*;
import manager.TaskManager;
import manager.Managers;
import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;


import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {

    private static final int PORT = 8080;
    private static HttpServer httpServer;
    private TaskManager manager;
    private HttpServer testServer;

    public HttpTaskServer(TaskManager manager) throws IOException {
        this.manager = manager;
        this.testServer =  HttpServer.create(new InetSocketAddress(8080), 0);
        testServer.createContext("/tasks", new TasksHandler(this.manager));
        testServer.createContext("/subtasks", new SubtasksHandler(this.manager));
        testServer.createContext("/epics", new EpicsHandler(this.manager));
        testServer.createContext("/history", new HistoryHandler(this.manager));
        testServer.createContext("/prioritized", new PrioritizedHandler(this.manager));
    }

    public static void main(String[] args) throws IOException {
        TaskManager taskManager = Managers.getDefault();
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        createAllTasks(taskManager);
        httpServer.createContext("/tasks", new TasksHandler(taskManager));
        httpServer.createContext("/subtasks", new SubtasksHandler(taskManager));
        httpServer.createContext("/epics", new EpicsHandler(taskManager));
        httpServer.createContext("/history", new HistoryHandler(taskManager));
        httpServer.createContext("/prioritized", new PrioritizedHandler(taskManager));
        httpServer.start();

    }

    public void startServer() {
        testServer.start();
    }

    public void stopServer() {
        testServer.stop(1);
    }

    private static void createAllTasks(TaskManager taskManager) {
        taskManager.createTask(new Task(
                        "Простая задача 1",
                        "Описание простой задачи 1",
                        TaskStatus.NEW,
                        10,
                        "04.05.1983 12:00"
                )
        );
        taskManager.createTask(new Task(
                        "Простая задача 2",
                        "Описание простой задачи 2",
                        TaskStatus.IN_PROGRESS,
                        150,
                        "11.03.2025 14:25"
                )
        );
        taskManager.createEpic(new Epic("Эпик 1", "Описание эпика 1"));
        taskManager.createEpic(new Epic("Эпик 2", "Описание эпика 2"));
        taskManager.createSubtask(new Subtask(
                        "Подзадача 1",
                        "Описание подзадачи 1",
                        TaskStatus.NEW,
                        90,
                "11.03.2028 14:25",
                        3
                )
        );
        taskManager.createSubtask(new Subtask(
                        "Подзадача 2",
                        "Описание подзадачи 2",
                        TaskStatus.NEW,
                10,
                "10.03.2025 14:25",
                        3
                )
        );
        taskManager.createSubtask(new Subtask(
                        "Подзадача 3",
                        "Описание подзадачи 3",
                        TaskStatus.NEW,
                35,
                "11.04.2025 14:25",
                        4
                )
        );
        taskManager.updateTask(new Task(1,
                        "Простая задача 1",
                        "Описание простой задачи 1",
                        TaskStatus.DONE,
                        21,
                        "13.01.2026 13:47"
                )
        );
        taskManager.updateTask(new Task(2,
                        "Простая задача 15",
                        "Описание простой задачи 15",
                        TaskStatus.DONE,
                137,
                "13.01.2025 13:50"
                )
        );
        taskManager.updateSubtask(new Subtask(
                        7,
                        "Подзадача 3",
                        "Описание подзадачи 3",
                        TaskStatus.DONE,
                35,
                "11.04.2025 14:25",
                        4
                )
        );
        taskManager.updateSubtask(new Subtask(
                        5,
                        "Подзадача 1",
                        "Описание подзадачи 1",
                        TaskStatus.DONE,
                10,
                "10.03.2038 14:25",
                        3
                )
        );
        taskManager.updateSubtask(new Subtask(
                        6,
                        "Подзадача 2",
                        "Описание подзадачи 2",
                        TaskStatus.IN_PROGRESS,
                90,
                "11.03.2039 14:25",
                        3
                )
        );
        System.out.println("Приоритеты:");
        System.out.println(taskManager.getPrioritizedTasks());
    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getAllTasks()) {
            System.out.println(manager.getTaskById(task.getTaskID()));
        }
        System.out.println("Эпики:");
        for (Task epic : manager.getAllEpics()) {
            System.out.println(manager.getEpicById(epic.getTaskID()));
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getAllSubtasks()) {
            System.out.println(manager.getSubtaskById(subtask.getTaskID()));
        }
        System.out.println("Приоритеты:");
        System.out.println(manager.getPrioritizedTasks());

    }

}
