import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    private InMemoryTaskManager manager;
    @BeforeEach
    public void beforeEach() {
        manager = new InMemoryTaskManager();
    }
    @Test
    public void shouldCreateTask() {
        Task originalTask = new Task("Task", "Task" , TaskStatus.NEW);
        manager.createTask(originalTask);
        final ArrayList<Task> tasks = manager.getAllTasks();
        assertNotNull(tasks, "Не создан список задач");
        assertFalse(tasks.isEmpty(), "Задача не создана");
        assertEquals(1, tasks.size(), "Неверное количество задач");
        Task task = manager.getTaskById(1);
        assertNotNull(task, "Задача не существует");
        assertEquals(originalTask, task, "Задачи не совпадают");
        assertEquals(1, task.getID(), "Номер задачи не существует");
        assertEquals("Task", task.getTitle(), "Название задачи неверное");
        assertEquals("Task", task.getDescription(), "Описание задачи неверное");
        assertEquals(TaskStatus.NEW, task.getStatus(), "Статус задачи неверный");
    }

    @Test
    public void shouldCreateEpic() {
        Epic originalEpic = new Epic("Task", "Task");
        manager.createEpic(originalEpic);
        final ArrayList<Epic> epics = manager.getAllEpics();
        assertNotNull(epics, "Не создан список задач");
        assertFalse(epics.isEmpty(), "Задача не создана");
        assertEquals(1, epics.size(), "Неверное количество задач");
        Epic epic = manager.getEpicById(1);
        assertNotNull(epic, "Задача не существует");
        assertEquals(originalEpic, epic, "Задачи не совпадают");
        assertEquals(1, epic.getID(), "Номер задачи не существует");
        assertEquals("Task", epic.getTitle(), "Название задачи неверное");
        assertEquals("Task", epic.getDescription(), "Описание задачи неверное");
        assertEquals(TaskStatus.NEW, epic.getStatus(), "Статус задачи неверный");
    }

    @Test
    public void shouldCreateSubtask() {
        manager.createEpic(new Epic("Task", "Task"));
        Subtask originalSubtask = new Subtask("Task",
                "Task",
                TaskStatus.NEW,
                1);
        manager.createSubtask(originalSubtask);
        final ArrayList<Subtask> subtasks = manager.getAllSubtasks();
        assertNotNull(subtasks, "Не создан список задач");
        assertFalse(subtasks.isEmpty(), "Задача не создана");
        assertEquals(1, subtasks.size(), "Неверное количество задач");
        Subtask subtask = manager.getSubtaskById(2);
        assertNotNull(subtask, "Задача не существует");
        assertEquals(originalSubtask, subtask, "Задачи не совпадают");
        assertEquals(2, subtask.getID(), "Номер задачи не существует");
        assertEquals("Task", subtask.getTitle(), "Название задачи неверное");
        assertEquals("Task", subtask.getDescription(), "Описание задачи неверное");
        assertEquals(TaskStatus.NEW, subtask.getStatus(), "Статус задачи неверный");
    }

    @Test
    public void shouldNotBeConflictsBetweenManuallyCreatedIdAndGeneratedId() {
        Task task1 = new Task("Task1", "Task1", TaskStatus.NEW);
        Task task2 = new Task(1,
                "Manual task",
                "Manual Task",
                TaskStatus.NEW);
        manager.createTask(task1);
        manager.createTask(task2);
        ArrayList<Task> tasks = manager.getAllTasks();
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
        assertEquals(savedTask2.getTitle(),
                task2.getTitle(),
                "Неверная название второй задачи");
        assertEquals(savedTask2.getDescription(),
                task2.getDescription(),
                "Неверное описание второй задачи");
        assertEquals(savedTask2.getStatus(),
                task2.getStatus(),
                "Неверный статус второй задачи");
    }

    @Test
    public void shouldBeNoChangesInTaskAfterSaved() {
        Task task = new Task("Task1", "Task1", TaskStatus.NEW);
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
    }

    @Test
    public void shouldBeOldVersionOfTaskInHistoryManager() {
        Task task = new Task("Task", "Task", TaskStatus.NEW);
        manager.createTask(task);
        Task savedTask = manager.getTaskById(1);
        Task savedInHistoryTask = manager.getHistory().get(0);
        manager.updateTask(new Task(1,"Updated", "Updated", TaskStatus.DONE));
        assertEquals("Task",
                savedInHistoryTask.getTitle(),
                "Неверное название задачи");
        assertEquals("Task",
                savedInHistoryTask.getDescription(),
                "Неверное описание задачи");
        assertEquals(TaskStatus.NEW,
                savedInHistoryTask.getStatus(),
                "Неверный статус");
    }

    @Test
    public void shouldUpdateTask() {
        manager.createTask(new Task("Title", "Description", TaskStatus.NEW));
        manager.updateTask(new Task(1, "Title1", "Description1", TaskStatus.DONE));
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
    }
    @Test
    public void shouldUpdateSubtask() {
        manager.createEpic(new Epic("Title", "Description"));
        manager.createSubtask(new Subtask("Title", "Description", TaskStatus.NEW, 1));
        manager.updateSubtask(new Subtask(2,
                "Title1",
                "Description1",
                TaskStatus.DONE,
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
    }

    @Test
    public void shouldUpdatedEpicIfSubtaskStatusUpdated() {
        manager.createEpic(new Epic("Title", "Description"));
        manager.createSubtask(new Subtask("Title", "Description", TaskStatus.NEW, 1));
        manager.updateSubtask(new Subtask(2,
                "Title",
                "Description",
                TaskStatus.DONE,
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
    }

    @Test
    public void shouldDeleteTaskById() {
        manager.createTask(new Task("Title", "Description", TaskStatus.DONE));
        manager.deleteTaskByID(manager.getTaskById(1).getID());
        ArrayList<Task> tasks = manager.getAllTasks();
        assertNotNull(tasks);
        assertTrue(tasks.isEmpty());
    }

    @Test
    public void shouldDeleteEpicById() {
        manager.createEpic(new Epic("Title", "Description"));
        manager.deleteEpicByID(manager.getEpicById(1).getID());
        ArrayList<Epic> epics = manager.getAllEpics();
        assertNotNull(epics);
        assertTrue(epics.isEmpty());
    }

    @Test
    public void shouldDeleteSubtaskById() {
        manager.createEpic(new Epic("Title", "Description"));
        manager.createSubtask(new Subtask("Title", "Description", TaskStatus.DONE, 1));
        manager.deleteSubtaskByID(manager.getSubtaskById(2).getID());
        ArrayList<Subtask> subtasks = manager.getAllSubtasks();
        assertNotNull(subtasks);
        assertTrue(subtasks.isEmpty());
    }

    @Test
    public void shouldDeleteAllTasks() {
        manager.createTask(new Task("Title", "Description", TaskStatus.DONE));
        manager.createTask(new Task("Title1", "Description1", TaskStatus.DONE));
        ArrayList<Task> tasks = manager.getAllTasks();
        assertNotNull(tasks);
        assertEquals(2, tasks.size());
        manager.deleteAllTasks();
        tasks = manager.getAllTasks();
        assertNotNull(tasks);
        assertTrue(tasks.isEmpty());
    }

    @Test
    public void shouldDeleteAllEpics() {
        manager.createEpic(new Epic("Title", "Description"));
        manager.createEpic(new Epic("Title", "Description"));
        ArrayList<Epic> epics = manager.getAllEpics();
        assertNotNull(epics);
        assertEquals(2, epics.size());
        manager.deleteAllEpics();
        epics = manager.getAllEpics();
        assertNotNull(epics);
        assertTrue(epics.isEmpty());
    }

    @Test
    public void shouldDeleteAllSubtasks() {
        manager.createEpic(new Epic("Title", "Description"));
        manager.createSubtask(new Subtask("Title", "Description", TaskStatus.DONE, 1));
        manager.createSubtask(new Subtask("Title", "Description", TaskStatus.DONE, 1));
        ArrayList<Subtask> subtasks = manager.getAllSubtasks();
        assertNotNull(subtasks);
        assertEquals(2, subtasks.size());
        manager.deleteAllSubtasks();
        subtasks = manager.getAllSubtasks();
        assertNotNull(subtasks);
        assertTrue(subtasks.isEmpty());
    }
}