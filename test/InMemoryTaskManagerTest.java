import manager.InMemoryTaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void beforeEach() {
        manager = new InMemoryTaskManager();
    }

    @Test
    public void shouldNotAddTaskOrSubtaskWhichIntersectsWithExisting() {
        manager.createTask(new Task("Title", "Description", TaskStatus.DONE,
                30, "05.09.2025 14:28"));
        manager.createTask(new Task("Title", "Description", TaskStatus.DONE,
                30, "05.09.2025 14:40"));
        manager.createEpic(new Epic(2,"Title", "Description"));
        manager.createSubtask(new Subtask("Title", "Description", TaskStatus.DONE,
                30, "05.09.2025 16:28", 2));
        manager.createSubtask(new Subtask("Title", "Description", TaskStatus.DONE,
                30, "05.09.2025 15:59", 2));
        assertEquals(2, manager.getPrioritizedTasks().size());
    }

}