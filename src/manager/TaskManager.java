package manager;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.List;
import java.util.Set;

public interface TaskManager {
    List<Task> getAllTasks();

    List<Subtask> getAllSubtasks();

    List<Epic> getAllEpics();

    void createTask(Task task);

    void createSubtask(Subtask subtask);

    void createEpic(Epic epic);

    void updateTask(Task task);

    void updateSubtask(Subtask newSubtask);

    void updateEpic(Epic epic);

    void deleteTaskByID(int taskID);

    void deleteSubtaskByID(int taskID);

    void deleteEpicByID(int taskID);

    void deleteAllTasks();

    void deleteAllSubtasks();

    void deleteAllEpics();

    Task getTaskById(int taskID);

    Subtask getSubtaskById(int taskID);

    Epic getEpicById(int taskID);

    List<Task> getHistory();

    List<Subtask> getEpicsSubtasks(int taskID);

    Set<Task> getPrioritizedTasks();

    boolean checkIntersections(Task task);

    boolean ifStartTimeIsNull(Task task);
}
