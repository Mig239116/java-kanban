import model.Epic;
import model.Subtask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EpicTest {
    @Test
    public void shouldBeEqualIfSameId() {
        Epic epic1 = new Epic(1, "Epic1", "First epic");
        Epic epic2 = new Epic(1, "Epic2", "Second epic");
        Assertions.assertEquals(epic1, epic2, "Экземпляры не равны");
    }

    @Test
    public void cannotBeAddedAsOwnSubtask() {
        Epic epic = new Epic(1, "Epic1", "First epic");
        epic.addSubtask(new Subtask(epic.getID(), epic.getTitle(), epic.getDescription(), epic.getStatus(), epic.getID()));
        Assertions.assertTrue(epic.getSubtaskReferences().isEmpty());
    }
}