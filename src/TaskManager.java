import java.util.ArrayList;

public interface TaskManager {
    ArrayList<Task> getAllTasks();

    ArrayList<Subtask> getAllSubtasks();

    ArrayList<Epic> getAllEpics();

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

    ArrayList<Task> getHistory();

    ArrayList<Subtask> getEpicsSubtasks(int taskID);
}
