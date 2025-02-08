import manager.InMemoryTaskManager;
import manager.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

abstract class TaskManagerTest<T extends TaskManager> {
    protected T manager;

    @Test
    public void shouldCreateTask() {
        Task originalTask = new Task("model.Task", "model.Task" , TaskStatus.NEW,
                35, "04.06.1998 14:28");
        manager.createTask(originalTask);
        final List<Task> tasks = manager.getAllTasks();
        assertNotNull(tasks, "Не создан список задач");
        assertFalse(tasks.isEmpty(), "Задача не создана");
        assertEquals(1, tasks.size(), "Неверное количество задач");
        Task task = manager.getTaskById(1);
        assertNotNull(task, "Задача не существует");
        assertEquals(originalTask, task, "Задачи не совпадают");
        assertEquals(1, task.getID(), "Номер задачи не существует");
        assertEquals("model.Task", task.getTitle(), "Название задачи неверное");
        assertEquals("model.Task", task.getDescription(), "Описание задачи неверное");
        assertEquals(TaskStatus.NEW, task.getStatus(), "Статус задачи неверный");
        assertEquals(35, task.getDurationNumeric(), "Длительность задачи неверный");
        assertEquals("04.06.1998 14:28", task.getStartTimeText(), "Дата начала задачи неверный");
    }

    @Test
    public void shouldCreateEpic() {
        Epic originalEpic = new Epic("model.Task", "model.Task");
        manager.createEpic(originalEpic);
        final List<Epic> epics = manager.getAllEpics();
        assertNotNull(epics, "Не создан список задач");
        assertFalse(epics.isEmpty(), "Задача не создана");
        assertEquals(1, epics.size(), "Неверное количество задач");
        Epic epic = manager.getEpicById(1);
        assertNotNull(epic, "Задача не существует");
        assertEquals(originalEpic, epic, "Задачи не совпадают");
        assertEquals(1, epic.getID(), "Номер задачи не существует");
        assertEquals("model.Task", epic.getTitle(), "Название задачи неверное");
        assertEquals("model.Task", epic.getDescription(), "Описание задачи неверное");
        assertEquals(TaskStatus.NEW, epic.getStatus(), "Статус задачи неверный");
        assertEquals(0, epic.getDurationNumeric(), "Длительность задачи неверный");
        assertEquals("01.01.1900 00:00", epic.getStartTimeText(), "Дата начала задачи неверный");
    }

    @Test
    public void shouldCreateSubtask() {
        manager.createEpic(new Epic("model.Task", "model.Task"));
        Subtask originalSubtask = new Subtask("model.Task",
                "model.Task",
                TaskStatus.NEW,
                45,
                "04.08.1998 14:28",
                1);
        manager.createSubtask(originalSubtask);
        final List<Subtask> subtasks = manager.getAllSubtasks();
        assertNotNull(subtasks, "Не создан список задач");
        assertFalse(subtasks.isEmpty(), "Задача не создана");
        assertEquals(1, subtasks.size(), "Неверное количество задач");
        Subtask subtask = manager.getSubtaskById(2);
        assertNotNull(subtask, "Задача не существует");
        assertEquals(originalSubtask, subtask, "Задачи не совпадают");
        assertEquals(2, subtask.getID(), "Номер задачи не существует");
        assertEquals("model.Task", subtask.getTitle(), "Название задачи неверное");
        assertEquals("model.Task", subtask.getDescription(), "Описание задачи неверное");
        assertEquals(TaskStatus.NEW, subtask.getStatus(), "Статус задачи неверный");
        assertEquals(45, subtask.getDurationNumeric(), "Длительность задачи неверный");
        assertEquals("04.08.1998 14:28", subtask.getStartTimeText(), "Дата задачи неверный");
    }

    @Test
    public void shouldNotBeConflictsBetweenManuallyCreatedIdAndGeneratedId() {
        Task task1 = new Task("Task1", "Task1", TaskStatus.NEW,
                25, "04.06.1998 14:28");
        Task task2 = new Task(1,
                "Manual task",
                "Manual model.Task",
                TaskStatus.NEW,
                28,
                "04.12.1998 14:28");
        manager.createTask(task1);
        manager.createTask(task2);
        List<Task> tasks = manager.getAllTasks();
        assertNotNull(tasks, "Не создан список задач");
        assertFalse(tasks.isEmpty(), "Задачи не создана");
        assertEquals(2, tasks.size(), "Неверное количество задач");
        Task savedTask1 = manager.getTaskById(1);
        Task savedTask2 = manager.getTaskById(2);
        assertNotEquals(savedTask1, savedTask2, "Задачи конфликтуют");
        assertEquals(savedTask1.getTitle(),
                task1.getTitle(),
                "Неверная название первой задачи");
        assertEquals(savedTask1.getDescription(),
                task1.getDescription(),
                "Неверное описание первой задачи");
        assertEquals(savedTask1.getStatus(),
                task1.getStatus(),
                "Неверный статус первой задачи");
        assertEquals(savedTask1.getDurationNumeric(),
                task1.getDurationNumeric(),
                "Неверная длительность первой задачи");
        assertEquals(savedTask1.getStartTimeText(),
                task1.getStartTimeText(),
                "Неверная стартовая дата первой задачи");
        assertEquals(savedTask2.getTitle(),
                task2.getTitle(),
                "Неверная название второй задачи");
        assertEquals(savedTask2.getDescription(),
                task2.getDescription(),
                "Неверное описание второй задачи");
        assertEquals(savedTask2.getStatus(),
                task2.getStatus(),
                "Неверный статус второй задачи");
        assertEquals(savedTask2.getDurationNumeric(),
                task2.getDurationNumeric(),
                "Неверная длительность второй задачи");
        assertEquals(savedTask2.getStartTimeText(),
                task2.getStartTimeText(),
                "Неверная стартовая дата второй задачи");
    }

    @Test
    public void shouldBeNoChangesInTaskAfterSaved() {
        Task task = new Task("Task1", "Task1", TaskStatus.NEW,
                30, "04.09.1999 14:28");
        manager.createTask(task);
        Task savedTask = manager.getTaskById(1);
        assertEquals(1,
                savedTask.getID(), "Неверный идентификатор задачи");
        assertEquals(savedTask.getTitle(),
                task.getTitle(),
                "Неверная название задачи");
        assertEquals(savedTask.getDescription(),
                task.getDescription(),
                "Неверное описание задачи");
        assertEquals(savedTask.getStatus(),
                task.getStatus(),
                "Неверный статус задачи");
        assertEquals(savedTask.getDurationNumeric(),
                task.getDurationNumeric(),
                "Неверная длительность задачи");
        assertEquals(savedTask.getStartTimeText(),
                task.getStartTimeText(),
                "Неверная дата начала задачи");
    }

    @Test
    public void shouldBeOldVersionOfTaskInHistoryManager() {
        Task task = new Task("model.Task", "model.Task", TaskStatus.NEW,
                30, "04.09.1999 14:28");
        manager.createTask(task);
        Task savedTask = manager.getTaskById(1);
        Task savedInHistoryTask = manager.getHistory().get(0);
        manager.updateTask(new Task(1,"Updated", "Updated", TaskStatus.DONE,
                60, "04.09.2025 14:28"));
        assertEquals("model.Task",
                savedInHistoryTask.getTitle(),
                "Неверное название задачи");
        assertEquals("model.Task",
                savedInHistoryTask.getDescription(),
                "Неверное описание задачи");
        assertEquals(TaskStatus.NEW,
                savedInHistoryTask.getStatus(),
                "Неверный статус");
        assertEquals(30,
                savedInHistoryTask.getDurationNumeric(),
                "Неверная длительность");
        assertEquals("04.09.1999 14:28",
                savedInHistoryTask.getStartTimeText(),
                "Неверная дата");
    }

    @Test
    public void shouldUpdateTask() {
        manager.createTask(new Task("Title", "Description", TaskStatus.NEW,
                30, "04.09.1999 14:28"));
        manager.updateTask(new Task(1, "Title1", "Description1", TaskStatus.DONE,
                60,"04.09.2025 14:28"));
        Task updatedTask = manager.getTaskById(1);
        assertNotNull(updatedTask);
        assertEquals(1,
                updatedTask.getID(),
                "Неверный идентификатор задачи");
        assertEquals("Title1",
                updatedTask.getTitle(),
                "Неверное название задачи");
        assertEquals("Description1",
                updatedTask.getDescription(),
                "Неверное описание задачи");
        assertEquals(TaskStatus.DONE,
                updatedTask.getStatus(),
                "Неверный статус");
        assertEquals(60,
                updatedTask.getDurationNumeric(),
                "Неверная длительность");
        assertEquals("04.09.2025 14:28",
                updatedTask.getStartTimeText(),
                "Неверная дата");
    }

    @Test
    public void shouldUpdateEpic() {
        manager.createEpic(new Epic("Title", "Description"));
        manager.updateEpic(new Epic(1, "Title1", "Description1"));
        Epic updatedEpic = manager.getEpicById(1);
        assertNotNull(updatedEpic);
        assertEquals(1,
                updatedEpic.getID(),
                "Неверный идентификатор задачи");
        assertEquals("Title1",
                updatedEpic.getTitle(),
                "Неверное название задачи");
        assertEquals("Description1",
                updatedEpic.getDescription(),
                "Неверное описание задачи");
        assertEquals(TaskStatus.NEW,
                updatedEpic.getStatus(),
                "Неверный статус");
        assertEquals(0,
                updatedEpic.getDurationNumeric(),
                "Неверная длительность");
        assertEquals("01.01.1900 00:00",
                updatedEpic.getStartTimeText(),
                "Неверная дата");
    }
    @Test
    public void shouldUpdateSubtask() {
        manager.createEpic(new Epic("Title", "Description"));
        manager.createSubtask(new Subtask("Title", "Description", TaskStatus.NEW,
                30, "04.09.2025 14:28", 1));
        manager.updateSubtask(new Subtask(2,
                "Title1",
                "Description1",
                TaskStatus.DONE,
                60,
                "05.09.2025 14:28",
                1));
        Subtask updatedSubtask = manager.getSubtaskById(2);
        assertNotNull(updatedSubtask);
        assertEquals(2,
                updatedSubtask.getID(),
                "Неверный идентификатор задачи");
        assertEquals("Title1",
                updatedSubtask.getTitle(),
                "Неверное название задачи");
        assertEquals("Description1",
                updatedSubtask.getDescription(),
                "Неверное описание задачи");
        assertEquals(TaskStatus.DONE,
                updatedSubtask.getStatus(),
                "Неверный статус");
        assertEquals(60,
                updatedSubtask.getDurationNumeric(),
                "Неверный длительность");
        assertEquals("05.09.2025 14:28",
                updatedSubtask.getStartTimeText(),
                "Неверный дата");
    }

    @Test
    public void shouldUpdatedEpicIfSubtaskStatusUpdated() {
        manager.createEpic(new Epic("Title", "Description"));
        manager.createSubtask(new Subtask("Title", "Description", TaskStatus.NEW,
                30, "04.09.2025 14:28", 1));
        manager.updateSubtask(new Subtask(2,
                "Title1",
                "Description1",
                TaskStatus.DONE,
                60,
                "05.09.2025 14:28",
                1));
        Epic updatedEpic = manager.getEpicById(1);
        Subtask updatedSubtask = manager.getSubtaskById(2);
        assertNotNull(updatedSubtask);
        assertNotNull(updatedEpic);
        assertEquals(TaskStatus.DONE,
                updatedSubtask.getStatus(),
                "Неверный статус");
        assertEquals(TaskStatus.DONE,
                updatedEpic.getStatus(),
                "Неверный статус");
        assertEquals(60,
                updatedEpic.getDurationNumeric(),
                "Неверный статус");
        assertEquals("05.09.2025 14:28",
                updatedEpic.getStartTimeText(),
                "Неверный статус");
    }

    @Test
    public void shouldDeleteTaskById() {
        manager.createTask(new Task("Title", "Description", TaskStatus.DONE,
                30, "05.09.2025 14:28"));
        manager.getTaskById(1);
        List<Task> history = manager.getHistory();
        assertEquals(1, history.size());
        manager.deleteTaskByID(manager.getTaskById(1).getID());
        List<Task> tasks = manager.getAllTasks();
        history = manager.getHistory();
        assertNotNull(tasks);
        assertTrue(tasks.isEmpty(), "Задача не удалена");
        assertTrue(history.isEmpty(), "Задача не удалена из истории просмотров");
    }

    @Test
    public void shouldDeleteEpicById() {
        manager.createEpic(new Epic("Title", "Description"));
        manager.getEpicById(1);
        List<Task> history = manager.getHistory();
        assertEquals(1, history.size());
        manager.deleteEpicByID(manager.getEpicById(1).getID());
        List<Epic> epics = manager.getAllEpics();
        history = manager.getHistory();
        assertNotNull(epics);
        assertTrue(epics.isEmpty(), "Эпик не удален");
        assertTrue(history.isEmpty(), "Эпик не удален из истории просмотров");
    }

    @Test
    public void shouldDeleteSubtaskById() {
        manager.createEpic(new Epic("Title", "Description"));
        manager.createSubtask(new Subtask("Title", "Description", TaskStatus.DONE,
                30, "05.09.2025 14:28", 1));
        manager.getSubtaskById(2);
        List<Task> history = manager.getHistory();
        assertEquals(1, history.size());
        manager.deleteSubtaskByID(manager.getSubtaskById(2).getID());
        List<Subtask> subtasks = manager.getAllSubtasks();
        history = manager.getHistory();
        assertNotNull(subtasks);
        assertTrue(subtasks.isEmpty(), "Подзадача не удалена");
        assertTrue(history.isEmpty(), "Подзадача не удалена из истории просмотров");
    }

    @Test
    public void shouldDeleteAllTasks() {
        manager.createTask(new Task("Title", "Description", TaskStatus.DONE,
                30, "05.09.2025 14:28"));
        manager.createTask(new Task("Title1", "Description1", TaskStatus.DONE, 30,
                "06.09.2025 14:28"));
        manager.getTaskById(1);
        manager.getTaskById(2);
        List<Task> history = manager.getHistory();
        assertEquals(2, history.size());
        List<Task> tasks = manager.getAllTasks();
        assertNotNull(tasks);
        assertEquals(2, tasks.size());
        manager.deleteAllTasks();
        history = manager.getHistory();
        tasks = manager.getAllTasks();
        assertNotNull(tasks);
        assertTrue(tasks.isEmpty());
        assertTrue(history.isEmpty());
    }

    @Test
    public void shouldDeleteAllEpics() {
        manager.createEpic(new Epic("Title", "Description"));
        manager.createEpic(new Epic("Title", "Description"));
        manager.getEpicById(1);
        manager.getEpicById(2);
        List<Task> history = manager.getHistory();
        assertEquals(2, history.size());
        List<Epic> epics = manager.getAllEpics();
        assertNotNull(epics);
        assertEquals(2, epics.size());
        manager.deleteAllEpics();
        epics = manager.getAllEpics();
        history = manager.getHistory();
        assertNotNull(epics);
        assertTrue(epics.isEmpty());
        assertTrue(history.isEmpty());
    }

    @Test
    public void shouldDeleteAllSubtasks() {
        manager.createEpic(new Epic("Title", "Description"));
        manager.createSubtask(new Subtask("Title", "Description", TaskStatus.DONE,
                30, "05.09.2025 14:28", 1));
        manager.createSubtask(new Subtask("Title", "Description", TaskStatus.DONE,
                30, "06.09.2025 14:28", 1));
        manager.getSubtaskById(2);
        manager.getSubtaskById(3);
        List<Task> history = manager.getHistory();
        assertEquals(2, history.size());
        List<Subtask> subtasks = manager.getAllSubtasks();
        assertNotNull(subtasks);
        assertEquals(2, subtasks.size());
        manager.deleteAllSubtasks();
        subtasks = manager.getAllSubtasks();
        history = manager.getHistory();
        assertNotNull(subtasks);
        assertTrue(subtasks.isEmpty());
        assertTrue(history.isEmpty());
    }
}