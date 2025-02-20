import manager.InMemoryHistoryManager;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    private InMemoryHistoryManager historyManager;

    @BeforeEach
    public void beforeEach() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    public void shouldAddTasks() {
        Task task = new Task(1, "Title", "Description", TaskStatus.DONE,
                30,
                "01.04.1998 14:28");
        historyManager.add(task);
        final ArrayList<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не создана");
        assertEquals(1, history.size(),"Кол-во элементов не совпадает с ожидаемым");
    }

    @Test
    public void shouldAddOnlyOneInstanceOfTask() {
        Task task = new Task(1, "Title", "Description", TaskStatus.DONE,
                30, "01.05.1998 14:28");
        historyManager.add(task);
        historyManager.add(task);
        final ArrayList<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не создана");
        assertEquals(1, history.size(),"Кол-во элементов не совпадает с ожидаемым");
    }

    @Test
    public void shouldReplaceTaskInTheEnd() {
        Task task1 = new Task(1, "Title", "Description", TaskStatus.DONE,
                45, "01.06.1998 14:28");
        Task task2 = new Task(2, "Title", "Description", TaskStatus.DONE,
                50, "02.06.1998 14:28");
        Task task3 = new Task(3, "Title", "Description", TaskStatus.DONE,
                60, "03.06.1998 14:28");
        Task task4 = new Task(3, "Title", "Description", TaskStatus.DONE,
                15, "04.06.1998 14:28");
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.add(task4);
        final ArrayList<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не создана");
        assertEquals(3, history.size(),"Кол-во элементов не совпадает с ожидаемым");
        assertEquals(history.get(2), task4, "Задание не совпадает с ожидаемым");
    }

    @Test
    public void shouldReplaceTaskInTheMiddle() {
        Task task1 = new Task(1, "Title", "Description", TaskStatus.DONE,
                45, "01.06.1998 14:28");
        Task task2 = new Task(2, "Title", "Description", TaskStatus.DONE,
                50, "02.06.1998 14:28");
        Task task3 = new Task(3, "Title", "Description", TaskStatus.DONE,
                60, "03.06.1998 14:28");
        Task task4 = new Task(2, "Title", "Description", TaskStatus.DONE,
                15, "04.06.1998 14:28");
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.add(task4);
        final ArrayList<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не создана");
        assertEquals(3, history.size(),"Кол-во элементов не совпадает с ожидаемым");
        assertEquals(history.get(1), task4, "Задание не совпадает с ожидаемым");
    }

    @Test
    public void shouldReplaceTaskInTheStart() {
        Task task1 = new Task(1, "Title", "Description", TaskStatus.DONE,
                45, "01.06.1998 14:28");
        Task task2 = new Task(2, "Title", "Description", TaskStatus.DONE,
                50, "02.06.1998 14:28");
        Task task3 = new Task(3, "Title", "Description", TaskStatus.DONE,
                60, "03.06.1998 14:28");
        Task task4 = new Task(1, "Title", "Description", TaskStatus.DONE,
                15, "04.06.1998 14:28");
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.add(task4);
        final ArrayList<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не создана");
        assertEquals(3, history.size(),"Кол-во элементов не совпадает с ожидаемым");
        assertEquals(history.get(0), task4, "Задание не совпадает с ожидаемым");
    }

    @Test
    public void shouldDeleteTaskById() {
        Task task1 = new Task(1, "Title", "Description", TaskStatus.DONE,
                15, "04.07.1998 14:28");
        historyManager.add(task1);
        historyManager.remove(1);
        final ArrayList<Task> history = historyManager.getHistory();
        assertTrue(history.isEmpty(), "История не пуста");
    }



}