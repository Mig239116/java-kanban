import model.Epic;
import model.Subtask;
import model.TaskStatus;
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
        epic.addSubtask(new Subtask(epic.getID(), epic.getTitle(), epic.getDescription(), epic.getStatus(),
                epic.getDurationNumeric(), epic.getStartTimeText(), epic.getID()));
        Assertions.assertTrue(epic.getSubtaskReferences().isEmpty());
    }

    @Test
    public void shouldHaveStatusSameAsAllSubtasksNEW() {
        Epic epic = new Epic(1, "Epic1", "First epic");
        Subtask subtask1 = new Subtask("Subtask 1", "Description 1", TaskStatus.NEW,
                30, "01.02.2000 14:28", 1);
        Subtask subtask2 = new Subtask("Subtask 2", "Description 2", TaskStatus.NEW,
                30, "01.02.2000 15:28", 1);
        Subtask subtask3 = new Subtask("Subtask 3", "Description 3", TaskStatus.NEW,
                30, "01.02.2000 16:28", 1);
        epic.addSubtask(subtask1);
        epic.addSubtask(subtask2);
        epic.addSubtask(subtask3);
        epic.updateStatus();
        Assertions.assertEquals(TaskStatus.NEW, epic.getStatus());
    }

    @Test
    public void shouldHaveStatusSameAsAllSubtasksDONE() {
        Epic epic = new Epic(1, "Epic1", "First epic");
        Subtask subtask1 = new Subtask("Subtask 1", "Description 1", TaskStatus.DONE,
                30, "01.02.2000 14:28", 1);
        Subtask subtask2 = new Subtask("Subtask 2", "Description 2", TaskStatus.DONE,
                30, "01.02.2000 15:28", 1);
        Subtask subtask3 = new Subtask("Subtask 3", "Description 3", TaskStatus.DONE,
                30, "01.02.2000 16:28", 1);
        epic.addSubtask(subtask1);
        epic.addSubtask(subtask2);
        epic.addSubtask(subtask3);
        epic.updateStatus();
        Assertions.assertEquals(TaskStatus.DONE, epic.getStatus());
    }

    @Test
    public void shouldHaveStatusSameAsAllSubtasksWithMixedStatus() {
        Epic epic = new Epic(1, "Epic1", "First epic");
        Subtask subtask1 = new Subtask("Subtask 1", "Description 1", TaskStatus.NEW,
                30, "01.02.2000 14:28", 1);
        Subtask subtask2 = new Subtask("Subtask 2", "Description 2", TaskStatus.DONE,
                30, "01.02.2000 15:28", 1);
        Subtask subtask3 = new Subtask("Subtask 3", "Description 3", TaskStatus.DONE,
                30, "01.02.2000 16:28", 1);
        epic.addSubtask(subtask1);
        epic.addSubtask(subtask2);
        epic.addSubtask(subtask3);
        epic.updateStatus();
        Assertions.assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void shouldHaveStatusSameAsAllSubtasksINPROGRESS() {
        Epic epic = new Epic(1, "Epic1", "First epic");
        Subtask subtask1 = new Subtask("Subtask 1", "Description 1", TaskStatus.IN_PROGRESS,
                30, "01.02.2000 14:28", 1);
        Subtask subtask2 = new Subtask("Subtask 2", "Description 2", TaskStatus.IN_PROGRESS,
                30, "01.02.2000 15:28", 1);
        Subtask subtask3 = new Subtask("Subtask 3", "Description 3", TaskStatus.IN_PROGRESS,
                30, "01.02.2000 16:28", 1);
        epic.addSubtask(subtask1);
        epic.addSubtask(subtask2);
        epic.addSubtask(subtask3);
        epic.updateStatus();
        Assertions.assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void shouldHaveDurationSameAsAllSubtasksSum() {
        Epic epic = new Epic(1, "Epic1", "First epic");
        Subtask subtask1 = new Subtask("Subtask 1", "Description 1", TaskStatus.IN_PROGRESS,
                30, "01.02.2000 14:28", 1);
        Subtask subtask2 = new Subtask("Subtask 2", "Description 2", TaskStatus.IN_PROGRESS,
                30, "01.02.2000 15:28", 1);
        Subtask subtask3 = new Subtask("Subtask 3", "Description 3", TaskStatus.IN_PROGRESS,
                30, "01.02.2000 16:28", 1);
        epic.addSubtask(subtask1);
        epic.addSubtask(subtask2);
        epic.addSubtask(subtask3);
        epic.updateStatus();
        epic.updateDuration();
        Assertions.assertEquals(90, epic.getDurationNumeric());
    }

    @Test
    public void shouldHaveStartTimeSameAsEarliestSubtask() {
        Epic epic = new Epic(1, "Epic1", "First epic");
        Subtask subtask1 = new Subtask("Subtask 1", "Description 1", TaskStatus.IN_PROGRESS,
                30, "01.02.2000 14:28", 1);
        Subtask subtask2 = new Subtask("Subtask 2", "Description 2", TaskStatus.IN_PROGRESS,
                30, "01.02.2000 15:28", 1);
        Subtask subtask3 = new Subtask("Subtask 3", "Description 3", TaskStatus.IN_PROGRESS,
                30, "01.02.2000 16:28", 1);
        epic.addSubtask(subtask1);
        epic.addSubtask(subtask2);
        epic.addSubtask(subtask3);
        epic.updateStatus();
        epic.updateDuration();
        epic.updateStartTime();
        Assertions.assertEquals("01.02.2000 14:28", epic.getStartTimeText());
    }
}