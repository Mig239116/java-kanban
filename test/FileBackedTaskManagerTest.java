import exceptions.ManagerLoadException;
import manager.FileBackedTaskManager;
import manager.InMemoryTaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
  private FileBackedTaskManager taskManager;
    @BeforeEach
    public void beforeEach() {
            taskManager = new FileBackedTaskManager(
                    new File(System.getProperty("user.home")+"/checkFile.csv").toString());
            manager = new InMemoryTaskManager();
    }

    @AfterEach
    public void afterEach() {
        taskManager.getAutoSaveFile().delete();
    }

    @Test
    public void shouldNotCreateFileIfNoData() {
        taskManager.createTask(new Task("model.Task", "model.Task" , TaskStatus.NEW,
                30, "01.02.1998 14:28"));
        taskManager.deleteTaskByID(1);
        File file = taskManager.getAutoSaveFile();
        assertFalse(file.exists(), "Создан пустой файл");
    }

    @Test
    public void shouldNotReadFromEmptyFile() {
        try {
            File file = File.createTempFile("testFile", "csv");
            FileBackedTaskManager testManager = FileBackedTaskManager.loadFromFile(file);
            assertNull(testManager, "Создается пустой менеджер заданий");
        } catch (IOException e) {

        }
    }

    @Test
    public void shouldCreateAndSaveDifferentTasks() {
        Task originalTask = new Task("model.Task", "model.Task" , TaskStatus.NEW,
                30, "01.02.1998 14:28");
        taskManager.createTask(originalTask);
        Epic originalEpic = new Epic("model.Task", "model.Task");
        taskManager.createEpic(originalEpic);
        Subtask originalSubtask = new Subtask("model.Task",
                "model.Task",
                TaskStatus.NEW,
                45,
                "01.03.1998 14:28",
                2);
        taskManager.createSubtask(originalSubtask);
        Task task = taskManager.getTaskById(1);
        Epic epic = taskManager.getEpicById(2);
        Subtask subtask = taskManager.getSubtaskById(3);
        FileBackedTaskManager newManager = FileBackedTaskManager.loadFromFile(taskManager.getAutoSaveFile());
        Task newTask = newManager.getTaskById(1);
        Epic newEpic = newManager.getEpicById(2);
        Subtask newSubtask = newManager.getSubtaskById(3);
        assertEquals(task, newTask, "Задания не равны");
        assertEquals(epic, newEpic, "Эпики не равны");
        assertEquals(subtask, newSubtask, "Подзадания не равны");
        assertEquals(1, newTask.getID(), "Номер задачи не существует");
        assertEquals("model.Task", newTask.getTitle(), "Название задачи неверное");
        assertEquals("model.Task", newTask.getDescription(), "Описание задачи неверное");
        assertEquals(TaskStatus.NEW, newTask.getStatus(), "Статус задачи неверный");
        assertEquals(2, newEpic.getID(), "Номер задачи не существует");
        assertEquals("model.Task", newEpic.getTitle(), "Название задачи неверное");
        assertEquals("model.Task", newEpic.getDescription(), "Описание задачи неверное");
        assertEquals(TaskStatus.NEW, newEpic.getStatus(), "Статус задачи неверный");
        assertEquals(3, newSubtask.getID(), "Номер задачи не существует");
        assertEquals("model.Task", newSubtask.getTitle(), "Название задачи неверное");
        assertEquals("model.Task", newSubtask.getDescription(), "Описание задачи неверное");
        assertEquals(TaskStatus.NEW, newSubtask.getStatus(), "Статус задачи неверный");
        assertEquals(3,
                newManager.getAllEpics().size() +
                        newManager.getAllTasks().size() +
                        newManager.getAllSubtasks().size(),
                "Количество задач не совпадает");
    }


}