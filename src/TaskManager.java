import java.util.HashMap;
import java.util.ArrayList;

public class TaskManager {
    private int tasksCounter;
    private TaskDatabase taskDatabase;

    public TaskManager() {
        tasksCounter = 0;
        taskDatabase = new TaskDatabase();
    }

    public HashMap<Integer, Task> getAllTasks() {
        return taskDatabase.tasks;
    }

    public HashMap<Integer, Subtask> getAllSubtasks() {
        return taskDatabase.subtasks;
    }

    public HashMap<Integer, Epic> getAllEpics() {
        return taskDatabase.epics;
    }

    public boolean isEpicExists(int taskID) {
        if (taskDatabase.epics.containsKey(taskID)) {
            return true;
        }
        return false;
    }

    public void createTask(Task task) {
        tasksCounter++;
        task.setID(tasksCounter);
        taskDatabase.tasks.put(task.getID(), task);
    }

    public void createSubtask(Subtask subtask) {
        tasksCounter++;
        subtask.setID(tasksCounter);
        taskDatabase.subtasks.put(subtask.getID(), subtask);
        System.out.println(subtask.getEpicReference());
        taskDatabase.epics.get(subtask.getEpicReference()).getSubtaskReferences().add(subtask);
        taskDatabase.epics.get(subtask.getEpicReference()).updateStatus();
    }



    public void createEpic(Epic epic) {
        tasksCounter++;
        epic.setID(tasksCounter);
        taskDatabase.epics.put(epic.getID(), epic);
    }

    public void updateTask(Task task) {
        if (taskDatabase.tasks.containsKey(task.getID())) taskDatabase.tasks.put(task.getID(), task);
    }

    public void updateSubtask(Subtask subtask) {
        if (taskDatabase.subtasks.containsKey(subtask.getID())) taskDatabase.subtasks.put(subtask.getID(), subtask);
        taskDatabase.epics.get(subtask.getEpicReference()).getSubtaskReferences().set(
                taskDatabase.epics.get(subtask.getEpicReference()).getSubtaskReferences().indexOf(subtask),
                subtask
        );
        taskDatabase.epics.get(subtask.getEpicReference()).updateStatus();
    }

    public void updateEpic(Epic epic) {
        if (taskDatabase.epics.containsKey(epic.getID())) taskDatabase.epics.put(epic.getID(), epic);
    }

    public void deleteTaskByID(int taskID) {
        if (taskDatabase.tasks.containsKey(taskID)) taskDatabase.tasks.remove(taskID);
    }

    public void deleteSubtaskByID(int taskID) {
        if (taskDatabase.subtasks.containsKey(taskID)) {
            Subtask subtask = taskDatabase.subtasks.get(taskID);
            taskDatabase.epics.get(subtask.getEpicReference()).getSubtaskReferences().remove(subtask);
            taskDatabase.epics.get(subtask.getEpicReference()).updateStatus();
            taskDatabase.subtasks.remove(taskID);
        }
    }

    public void deleteEpicByID(int taskID) {
        if (taskDatabase.epics.containsKey(taskID)) {
            for (Subtask subtask: taskDatabase.epics.get(taskID).getSubtaskReferences()) {
                taskDatabase.subtasks.remove(subtask.getID());
            }
            taskDatabase.epics.remove(taskID);
        }
    }

    public ArrayList<Subtask> getEpicsSubtasks(int taskID) {
        return taskDatabase.epics.get(taskID).getSubtaskReferences();
    }

    public void deleteAllTasks() {
        taskDatabase.tasks.clear();
    }

    public void deleteAllSubtasks() {
        for (Epic epic: taskDatabase.epics.values()) {
            epic.getSubtaskReferences().clear();
            epic.updateStatus();
        }
        taskDatabase.subtasks.clear();
    }

    public void deleteAllEpics() {
        taskDatabase.epics.clear();
        taskDatabase.subtasks.clear();
    }

    public Task getTaskById(int taskID) {
        if (taskDatabase.tasks.containsKey(taskID)) return taskDatabase.tasks.get(taskID);
        return null;
    }

    public Subtask getSubtaskById(int taskID) {
        if (taskDatabase.subtasks.containsKey(taskID)) return taskDatabase.subtasks.get(taskID);
        return null;
    }

    public Epic getEpicById(int taskID) {
        if (taskDatabase.epics.containsKey(taskID)) return taskDatabase.epics.get(taskID);
        return null;
    }


}
