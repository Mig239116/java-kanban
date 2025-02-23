import adapters.DurationAdapter;
import adapters.LocalDateAdapter;
import com.google.gson.*;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import support.EpicSpecificExclusionStrategies;
import support.EpicTestingExclusionStrategies;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerTasksTest {
    TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    static Gson gson;

    HttpTaskManagerTasksTest() throws IOException {
    }

    @BeforeAll
    public static void initiate() {
        gson = new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .excludeFieldsWithoutExposeAnnotation()
                .addDeserializationExclusionStrategy(new EpicSpecificExclusionStrategies())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
    }

    @BeforeEach
    public void setUp() {
        manager.deleteAllTasks();
        manager.deleteAllSubtasks();
        manager.deleteAllEpics();
        taskServer.startServer();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stopServer();
    }

    @Test
    public void shouldCreateTask() throws IOException, InterruptedException {
        Task originalTask = new Task("model.Task", "model.Task" , TaskStatus.NEW,
                35, "04.06.1998 14:28");
        String taskJson = gson.toJson(originalTask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        List<Task> tasksFromManager = manager.getAllTasks();
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        Task task = tasksFromManager.getFirst();
        assertEquals(1, task.getTaskID(), "Номер задачи не существует");
        assertEquals("model.Task", task.getTitle(), "Название задачи неверное");
        assertEquals("model.Task", task.getDescription(), "Описание задачи неверное");
        assertEquals(TaskStatus.NEW, task.getStatus(), "Статус задачи неверный");
        assertEquals(35, task.getDurationNumeric(), "Длительность задачи неверный");
        assertEquals("04.06.1998 14:28", task.getStartTimeText(), "Дата начала задачи неверный");
    }

    @Test
    public void shouldCreateSubtask() throws IOException, InterruptedException {
        manager.createEpic(new Epic("model.Task", "model.Task"));
        Subtask originalSubtask = new Subtask("model.Task",
                "model.Task",
                TaskStatus.NEW,
                45,
                "04.08.1998 14:28",
                1);
        String taskJson = gson.toJson(originalSubtask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        List<Subtask> tasksFromManager = manager.getAllSubtasks();
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        Subtask subtask = tasksFromManager.getFirst();
        assertEquals(2, subtask.getTaskID(), "Номер задачи не существует");
        assertEquals("model.Task", subtask.getTitle(), "Название задачи неверное");
        assertEquals("model.Task", subtask.getDescription(), "Описание задачи неверное");
        assertEquals(TaskStatus.NEW, subtask.getStatus(), "Статус задачи неверный");
        assertEquals(45, subtask.getDurationNumeric(), "Длительность задачи неверный");
        assertEquals("04.08.1998 14:28", subtask.getStartTimeText(), "Дата начала задачи неверный");
        assertEquals(1, subtask.getEpicReference(), "Не совпадает ссылка на эпик");
    }

    @Test
    public void shouldCreateEpic() throws IOException, InterruptedException{
        Gson gson1 = new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .excludeFieldsWithoutExposeAnnotation()
                .addDeserializationExclusionStrategy(new EpicSpecificExclusionStrategies())
                .addSerializationExclusionStrategy(new EpicTestingExclusionStrategies())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
        Epic originalEpic = new Epic("model.Task", "model.Task");
        String taskJson = gson1.toJson(originalEpic);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        List<Epic> tasksFromManager = manager.getAllEpics();
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        Epic epic = tasksFromManager.getFirst();
        assertEquals(1, epic.getTaskID(), "Номер задачи не существует");
        assertEquals("model.Task", epic.getTitle(), "Название задачи неверное");
        assertEquals("model.Task", epic.getDescription(), "Описание задачи неверное");
        assertEquals(TaskStatus.NEW, epic.getStatus(), "Статус задачи неверный");
        assertEquals(Duration.ofMinutes(0), epic.getDuration(), "Длительность задачи неверный");
        assertNull(epic.getStartTimeText(), "Дата начала задачи неверный");
    }

    @Test
    public void checkIfSubTasksIntersectsWIthOthers() throws IOException, InterruptedException{
        Task originalTask = new Task("model.Task", "model.Task" , TaskStatus.NEW,
                35, "04.06.1998 14:28");
        manager.createTask(originalTask);
        manager.createEpic(new Epic("model.Task", "model.Task"));
        Subtask originalSubtask = new Subtask("model.Task",
                "model.Task",
                TaskStatus.NEW,
                45,
                "04.06.1998 14:28",
                1);
        String taskJson = gson.toJson(originalSubtask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(406, response.statusCode());
        List<Subtask> subtaskList = manager.getAllSubtasks();
        assertEquals(0, subtaskList.size());
    }

    @Test
    public void checkIfTasksIntersectsWIthOthers() throws IOException, InterruptedException{
        Task originalTask = new Task("model.Task", "model.Task" , TaskStatus.NEW,
                35, "04.06.1998 14:28");
        manager.createEpic(new Epic("model.Task", "model.Task"));
        Subtask originalSubtask = new Subtask("model.Task",
                "model.Task",
                TaskStatus.NEW,
                45,
                "04.06.1998 14:28",
                1);
        manager.createSubtask(originalSubtask);
        String taskJson = gson.toJson(originalTask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(406, response.statusCode());
        List<Task> taskList = manager.getAllTasks();
        assertEquals(0, taskList.size());
    }

    @Test
    public void shouldReturnTask() throws IOException, InterruptedException{
        Task originalTask = new Task("model.Task", "model.Task" , TaskStatus.NEW,
                35, "04.06.1998 14:28");
        manager.createTask(originalTask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        assertTrue(jsonElement.isJsonObject(), "Ответ от сервера не соответсвует ожидаемому");
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        assertEquals("model.Task", jsonObject.get("title").getAsString());
        assertEquals("model.Task", jsonObject.get("description").getAsString());
        assertEquals(TaskStatus.NEW.toString(), jsonObject.get("status").getAsString());
        assertEquals(35, jsonObject.get("duration").getAsInt());
        assertEquals("04.06.1998 14:28", jsonObject.get("startTime").getAsString());
    }

    @Test
    public void shouldNotReturnTaskIfDoesNotExist() throws IOException, InterruptedException{
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
    }

    @Test
    public void shouldReturnAllTasks() throws IOException, InterruptedException{
        Task originalTask1 = new Task("model.Task1", "model.Task1" , TaskStatus.NEW,
                35, "04.06.1998 14:28");
        Task originalTask2 = new Task("model.Task2", "model.Task2" , TaskStatus.NEW,
                35, "04.06.2000 14:28");
        manager.createTask(originalTask1);
        manager.createTask(originalTask2);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        assertTrue(jsonElement.isJsonArray(), "Ответ от сервера не соответсвует ожидаемому");
        int i = 1;
        List<String> startDates = new ArrayList<String>();
        startDates.add("04.06.1998 14:28");
        startDates.add("04.06.2000 14:28");
        for (JsonElement jsonElem: jsonElement.getAsJsonArray()) {
            JsonObject jsonObject = jsonElem.getAsJsonObject();
            assertEquals("model.Task" + i, jsonObject.get("title").getAsString());
            assertEquals("model.Task" + i, jsonObject.get("description").getAsString());
            assertEquals(TaskStatus.NEW.toString(), jsonObject.get("status").getAsString());
            assertEquals(35, jsonObject.get("duration").getAsInt());
            assertEquals(startDates.get(i-1), jsonObject.get("startTime").getAsString());
            i++;
        }
    }

    @Test
    public void shouldReturnEpic() throws IOException, InterruptedException{
        Epic epic = new Epic("model.Task", "model.Task");
        manager.createEpic(epic);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        assertTrue(jsonElement.isJsonObject(), "Ответ от сервера не соответсвует ожидаемому");
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        assertEquals("model.Task", jsonObject.get("title").getAsString());
        assertEquals("model.Task", jsonObject.get("description").getAsString());
        assertEquals(TaskStatus.NEW.toString(), jsonObject.get("status").getAsString());
        assertEquals(0, jsonObject.get("duration").getAsInt());
        assertEquals(JsonNull.INSTANCE, jsonObject.get("startTime").getAsJsonNull());
    }

    @Test
    public void shouldNotReturnEpicIfDoesNotExist() throws IOException, InterruptedException{
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
    }

    @Test
    public void shouldReturnAllEpics() throws IOException, InterruptedException{
        Epic epic1 = new Epic("model.Task1", "model.Task1" );
        Epic epic2 = new Epic("model.Task2", "model.Task2");
        manager.createEpic(epic1);
        manager.createEpic(epic2);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        assertTrue(jsonElement.isJsonArray(), "Ответ от сервера не соответсвует ожидаемому");
        int i = 1;
        for (JsonElement jsonElem: jsonElement.getAsJsonArray()) {
            JsonObject jsonObject = jsonElem.getAsJsonObject();
            assertEquals("model.Task" + i, jsonObject.get("title").getAsString());
            assertEquals("model.Task" + i, jsonObject.get("description").getAsString());
            assertEquals(TaskStatus.NEW.toString(), jsonObject.get("status").getAsString());
            assertEquals(0, jsonObject.get("duration").getAsInt());
            assertEquals(JsonNull.INSTANCE, jsonObject.get("startTime").getAsJsonNull());
            i++;
        }
    }

    @Test
    public void shouldReturnSubtask() throws IOException, InterruptedException{
        Epic epic = new Epic("model.Task",
                "model.Task");
        manager.createEpic(epic);
        Subtask originalSubtask = new Subtask("model.Task",
                "model.Task",
                TaskStatus.NEW,
                45,
                "04.06.1998 14:28",
                1);
        manager.createSubtask(originalSubtask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/2");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        assertTrue(jsonElement.isJsonObject(), "Ответ от сервера не соответсвует ожидаемому");
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        assertEquals(2, jsonObject.get("taskID").getAsInt());
        assertEquals("model.Task", jsonObject.get("title").getAsString());
        assertEquals("model.Task", jsonObject.get("description").getAsString());
        assertEquals(TaskStatus.NEW.toString(), jsonObject.get("status").getAsString());
        assertEquals(45, jsonObject.get("duration").getAsInt());
        assertEquals("04.06.1998 14:28", jsonObject.get("startTime").getAsString());
        assertEquals(1, jsonObject.get("epicReference").getAsInt());
    }

    @Test
    public void shouldNotReturnSubtaskIfDoesNotExist() throws IOException, InterruptedException{
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
    }

    @Test
    public void shouldReturnAllSubtasks() throws IOException, InterruptedException{
        Epic epic1 = new Epic("model.Task1", "model.Task1" );
        Epic epic2 = new Epic("model.Task2", "model.Task2");
        manager.createEpic(epic1);
        manager.createEpic(epic2);
        Subtask subtask1 = new Subtask("model.Task1",
                "model.Task1",
                TaskStatus.NEW,
                45,
                "04.06.1998 14:28",
                1);
        manager.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("model.Task2",
                "model.Task2",
                TaskStatus.NEW,
                45,
                "04.06.2000 14:28",
                2);
        manager.createSubtask(subtask2);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        assertTrue(jsonElement.isJsonArray(), "Ответ от сервера не соответсвует ожидаемому");
        int i = 1;
        List<String> startDates = new ArrayList<String>();
        startDates.add("04.06.1998 14:28");
        startDates.add("04.06.2000 14:28");
        for (JsonElement jsonElem: jsonElement.getAsJsonArray()) {
            JsonObject jsonObject = jsonElem.getAsJsonObject();
            assertEquals("model.Task" + i, jsonObject.get("title").getAsString());
            assertEquals("model.Task" + i, jsonObject.get("description").getAsString());
            assertEquals(TaskStatus.NEW.toString(), jsonObject.get("status").getAsString());
            assertEquals(45, jsonObject.get("duration").getAsInt());
            assertEquals(startDates.get(i-1), jsonObject.get("startTime").getAsString());
            assertEquals(i, jsonObject.get("epicReference").getAsInt());
            i++;
        }
    }

    @Test
    public void shouldReturnAllEpicSubtasks() throws IOException, InterruptedException{
        Epic epic1 = new Epic("model.Task1", "model.Task1" );
        Epic epic2 = new Epic("model.Task2", "model.Task2");
        manager.createEpic(epic1);
        manager.createEpic(epic2);
        Subtask subtask1 = new Subtask("model.Task1",
                "model.Task1",
                TaskStatus.NEW,
                45,
                "04.06.1998 14:28",
                1);
        manager.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("model.Task2",
                "model.Task2",
                TaskStatus.NEW,
                45,
                "04.06.2000 14:28",
                1);
        manager.createSubtask(subtask2);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/1/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        assertTrue(jsonElement.isJsonArray(), "Ответ от сервера не соответсвует ожидаемому");
        int i = 1;
        List<String> startDates = new ArrayList<String>();
        startDates.add("04.06.1998 14:28");
        startDates.add("04.06.2000 14:28");
        for (JsonElement jsonElem: jsonElement.getAsJsonArray()) {
            JsonObject jsonObject = jsonElem.getAsJsonObject();
            assertEquals("model.Task" + i, jsonObject.get("title").getAsString());
            assertEquals("model.Task" + i, jsonObject.get("description").getAsString());
            assertEquals(TaskStatus.NEW.toString(), jsonObject.get("status").getAsString());
            assertEquals(45, jsonObject.get("duration").getAsInt());
            assertEquals(startDates.get(i-1), jsonObject.get("startTime").getAsString());
            assertEquals(1, jsonObject.get("epicReference").getAsInt());
            i++;
        }
    }

    @Test
    public void shouldDeleteTask() throws IOException, InterruptedException{
        Task originalTask = new Task("model.Task1", "model.Task1" , TaskStatus.NEW,
                35, "04.06.1998 14:28");
        manager.createTask(originalTask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Task> taskList = manager.getAllTasks();
        assertEquals(0, taskList.size());
    }

    @Test
    public void shouldNotDeleteTask() throws IOException, InterruptedException{
        Task originalTask = new Task("model.Task1", "model.Task1" , TaskStatus.NEW,
                35, "04.06.1998 14:28");
        manager.createTask(originalTask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/2");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
        List<Task> taskList = manager.getAllTasks();
        assertEquals(1, taskList.size());
        for (Task task: taskList) {
            assertNotEquals(2, task.getTaskID());
        }
    }

    @Test
    public void shouldDeleteEpic() throws IOException, InterruptedException{
        Epic epic = new Epic("model.Task1", "model.Task1");
        manager.createEpic(epic);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Epic> taskList = manager.getAllEpics();
        assertEquals(0, taskList.size());
    }

    @Test
    public void shouldNotDeleteEpic() throws IOException, InterruptedException{
        Epic epic = new Epic("model.Task1", "model.Task1");
        manager.createEpic(epic);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/2");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
        List<Epic> taskList = manager.getAllEpics();
        assertEquals(1, taskList.size());
        for (Epic task: taskList) {
            assertNotEquals(2, task.getTaskID());
        }
    }

    @Test
    public void shouldDeleteSubtask() throws IOException, InterruptedException{
        Epic epic = new Epic("asda", "asdasd");
        manager.createEpic(epic);
        Subtask originalSubtask = new Subtask("model.Task1", "model.Task1" , TaskStatus.NEW,
                35, "04.06.1998 14:28", 1);
        manager.createSubtask(originalSubtask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/2");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        List<Subtask> taskList = manager.getAllSubtasks();
        assertEquals(0, taskList.size());
    }

    @Test
    public void shouldNotDeleteSubtask() throws IOException, InterruptedException{
        Epic epic = new Epic("asda", "asdasd");
        manager.createEpic(epic);
        Subtask originalTask = new Subtask("model.Task1", "model.Task1" , TaskStatus.NEW,
                35, "04.06.1998 14:28", 1);
        manager.createSubtask(originalTask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
        List<Subtask> taskList = manager.getAllSubtasks();
        assertEquals(1, taskList.size());
        for (Subtask task: taskList) {
            assertNotEquals(1, task.getTaskID());
        }
    }

}

