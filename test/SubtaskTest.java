import model.Epic;
import model.Subtask;
import model.TaskStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SubtaskTest {

    @Test
    public void shouldBeEqualIfSameId() {
        Epic epic = new Epic(1, "Epic1", "First epic");
        Subtask subtask1 = new Subtask(2, "Subtask1", "First subtask", TaskStatus.DONE,
                30, "01.02.1998 14:28", 1);
        Subtask subtask2 = new Subtask(2, "Subtask2", "Second subtask", TaskStatus.IN_PROGRESS,
                45, "02.02.1998 14:28", 1);
        Assertions.assertEquals(subtask1, subtask2, "Экземпляры не равны");
    }

    @Test
    public void cannotAddAsOwnEpic() {
        Subtask subtask = new Subtask(1, "model.Subtask", "First subtask", TaskStatus.DONE,
                30, "01.02.1998 14:28", 1);
        Epic epic = new Epic(subtask.getTaskID(), subtask.getTitle(), subtask.getDescription());
        epic.addSubtask(subtask);
        Assertions.assertTrue(epic.getSubtaskReferences().isEmpty());
    }



}