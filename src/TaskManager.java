import java.util.ArrayList;

public class TaskManager {
    private int tasksCounter;
    private TaskDatabase taskDatabase;

    public TaskManager() {
        tasksCounter = 0;
        taskDatabase = new TaskDatabase();
    }

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(taskDatabase.tasks.values());
    }

    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(taskDatabase.subtasks.values());
    }

    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(taskDatabase.epics.values());
    }

    private boolean isEpicExists(int taskID) {
        return taskDatabase.epics.containsKey(taskID);
    }

    private boolean isSubtaskExists(int taskID) {
        return taskDatabase.subtasks.containsKey(taskID);
    }

    private boolean isSubtaskEpicReferenceValid(Subtask newSubtask) {
        Subtask existingSubtask = taskDatabase.subtasks.get(newSubtask.getID());
        return existingSubtask.getEpicReference() == newSubtask.getEpicReference();
    }

    public void createTask(Task task) {
        tasksCounter++;
        task.setID(tasksCounter);
        taskDatabase.tasks.put(task.getID(), task);
    }

    public void createSubtask(Subtask subtask) {
        if (this.isEpicExists(subtask.getEpicReference())) {
            tasksCounter++;
            subtask.setID(tasksCounter);
            taskDatabase.subtasks.put(subtask.getID(), subtask);
            Epic currentEpic = taskDatabase.epics.get(subtask.getEpicReference());
            currentEpic.addSubtask(subtask);
            currentEpic.updateStatus();
        }
    }



    public void createEpic(Epic epic) {
        tasksCounter++;
        epic.setID(tasksCounter);
        taskDatabase.epics.put(epic.getID(), epic);
    }

    public void updateTask(Task task) {
        if (taskDatabase.tasks.containsKey(task.getID())) taskDatabase.tasks.put(task.getID(), task);
    }

    public void updateSubtask(Subtask newSubtask) {
        if (this.isSubtaskExists(newSubtask.getID()) && this.isSubtaskEpicReferenceValid(newSubtask)) {
            Epic currentEpic = taskDatabase.epics.get(newSubtask.getEpicReference());
            Subtask oldSubtask = currentEpic.getSubtask(newSubtask.getID());
            currentEpic.updateSubtask(oldSubtask, newSubtask);
            taskDatabase.subtasks.put(newSubtask.getID(), newSubtask);
            currentEpic.updateStatus();
        }
    }

    public void updateEpic(Epic epic) {
        if (this.isEpicExists(epic.getID())) {
            Epic currentEpic = taskDatabase.epics.get(epic.getID());
            currentEpic.setTitle(epic.getTitle());
            currentEpic.setDescription(epic.getDescription());
        }
    }

    public void deleteTaskByID(int taskID) {
        if (taskDatabase.tasks.containsKey(taskID)) taskDatabase.tasks.remove(taskID);
    }

    public void deleteSubtaskByID(int taskID) {
        if (this.isSubtaskExists(taskID)) {
            Subtask subtask = taskDatabase.subtasks.get(taskID);
            Epic currentEpic =taskDatabase.epics.get(subtask.getEpicReference());
            currentEpic.deleteSubtask(subtask);
            currentEpic.updateStatus();
            taskDatabase.subtasks.remove(taskID);
        }
    }

    public void deleteEpicByID(int taskID) {
        if (this.isEpicExists(taskID)) {
            for (Subtask subtask: taskDatabase.epics.get(taskID).getSubtaskReferences()) {
                taskDatabase.subtasks.remove(subtask.getID());
            }
            taskDatabase.epics.remove(taskID);
        }
    }

    public ArrayList<Subtask> getEpicsSubtasks(int taskID) {
        if (this.isEpicExists(taskID)) {
            return taskDatabase.epics.get(taskID).getSubtaskReferences();
        }
        return null;
    }

    public void deleteAllTasks() {
        taskDatabase.tasks.clear();
    }

    public void deleteAllSubtasks() {
        for (Epic epic: taskDatabase.epics.values()) {
            epic.clearAllSubtasks();
            epic.updateStatus();
        }
        taskDatabase.subtasks.clear();
    }

    public void deleteAllEpics() {
        taskDatabase.epics.clear();
        taskDatabase.subtasks.clear();
    }

    public Task getTaskById(int taskID) {
        return taskDatabase.tasks.get(taskID);
    }

    public Subtask getSubtaskById(int taskID) {
        return taskDatabase.subtasks.get(taskID);
    }

    public Epic getEpicById(int taskID) {
        return taskDatabase.epics.get(taskID);
    }


}
