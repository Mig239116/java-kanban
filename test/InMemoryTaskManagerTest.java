import exceptions.IntersectionException;
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
        try {
            manager.createTask(new Task("Title", "Description", TaskStatus.DONE,
                    30, "05.09.2025 14:28"));
            manager.createTask(new Task("Title", "Description", TaskStatus.DONE,
                    30, "05.09.2025 14:40"));
        } catch (IntersectionException e) {
            assertEquals(1, manager.getPrioritizedTasks().size());
        }
    }

}