import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TaskTest {
    @Test
    public void shouldBeEqualIfSameId() {
        Task task1 = new Task(1, "Task1", "First task", TaskStatus.NEW,
                30, "01.02.1998 14:28");
        Task task2 = new Task(1, "Task2", "Second task", TaskStatus.IN_PROGRESS,
                45, "01.02.1998 14:28");
        Assertions.assertEquals(task1, task2, "Экземпляры не равны");
    }
}