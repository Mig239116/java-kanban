import manager.HistoryManager;
import manager.Managers;
import manager.TaskManager;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    public void shouldReturnTaskManagerInstancesReadyForUse() {
        Managers managers = new Managers();
        TaskManager taskManager = managers.getDefault();
        assertNotNull(taskManager, "Не создан менеджер задач");
        assertNotNull(taskManager.getHistory(), "Не создан список истории");
        assertNotNull(taskManager.getAllTasks(), "Не создан список задач");
        assertNotNull(taskManager.getAllEpics(), "Не создан список эпиков");
        assertNotNull(taskManager.getAllSubtasks(), "Не создан список подзадач");
        assertTrue(taskManager.getAllEpics().isEmpty(), "Список эпиков не пуст");
        assertTrue(taskManager.getHistory().isEmpty(), "История не пуста");
        assertTrue(taskManager.getAllTasks().isEmpty(), "Список задач не пуст");
        assertTrue(taskManager.getAllSubtasks().isEmpty(), "Список подзадач не пуст");
        taskManager.createTask(new Task("task1", "task1", TaskStatus.NEW));
        assertEquals(1, taskManager.getTaskById(1).getID(),
                "Генератор идентификаторов работает неверно");
    }

    @Test
    public void shouldReturnHistoryManagerInstancesReadyForUse() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        assertNotNull(historyManager, "Не создан менеджер истории");
        assertNotNull(historyManager.getHistory(), "Не создан список истории");
        assertTrue(historyManager.getHistory().isEmpty(), "История не пуста");
    }

}