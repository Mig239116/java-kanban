package manager;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;

public class InMemoryTaskManager implements TaskManager {
    private int tasksCounter;
    private TaskDatabase taskDatabase;
    private HistoryManager historyManager;

    public InMemoryTaskManager() {
        tasksCounter = 0;
        taskDatabase = new TaskDatabase();
        historyManager = Managers.getDefaultHistory();
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(taskDatabase.tasks.values());
    }

    @Override
    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(taskDatabase.subtasks.values());
    }

    @Override
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

    @Override
    public void createTask(Task task) {
        tasksCounter++;
        task.setID(tasksCounter);
        taskDatabase.tasks.put(task.getID(), task);
    }

    @Override
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



    @Override
    public void createEpic(Epic epic) {
        tasksCounter++;
        epic.setID(tasksCounter);
        taskDatabase.epics.put(epic.getID(), epic);
    }

    @Override
    public void updateTask(Task task) {
        if (taskDatabase.tasks.containsKey(task.getID())) taskDatabase.tasks.put(task.getID(), task);
    }

    @Override
    public void updateSubtask(Subtask newSubtask) {
        if (this.isSubtaskExists(newSubtask.getID()) && this.isSubtaskEpicReferenceValid(newSubtask)) {
            Epic currentEpic = taskDatabase.epics.get(newSubtask.getEpicReference());
            Subtask oldSubtask = currentEpic.getSubtask(newSubtask.getID());
            currentEpic.updateSubtask(oldSubtask, newSubtask);
            taskDatabase.subtasks.put(newSubtask.getID(), newSubtask);
            currentEpic.updateStatus();
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (this.isEpicExists(epic.getID())) {
            Epic currentEpic = taskDatabase.epics.get(epic.getID());
            currentEpic.setTitle(epic.getTitle());
            currentEpic.setDescription(epic.getDescription());
        }
    }

    @Override
    public void deleteTaskByID(int taskID) {
        if (taskDatabase.tasks.containsKey(taskID)) {
            historyManager.remove(taskID);
            taskDatabase.tasks.remove(taskID);
        }
    }

    @Override
    public void deleteSubtaskByID(int taskID) {
        if (this.isSubtaskExists(taskID)) {
            Subtask subtask = taskDatabase.subtasks.get(taskID);
            Epic currentEpic = taskDatabase.epics.get(subtask.getEpicReference());
            currentEpic.deleteSubtask(subtask);
            currentEpic.updateStatus();
            historyManager.remove(taskID);
            taskDatabase.subtasks.remove(taskID);
        }
    }

    @Override
    public void deleteEpicByID(int taskID) {
        if (this.isEpicExists(taskID)) {
            for (Subtask subtask: taskDatabase.epics.get(taskID).getSubtaskReferences()) {
                historyManager.remove(subtask.getID());
                taskDatabase.subtasks.remove(subtask.getID());
            }
            historyManager.remove(taskID);
            taskDatabase.epics.remove(taskID);
        }
    }

    @Override
    public ArrayList<Subtask> getEpicsSubtasks(int taskID) {
        if (this.isEpicExists(taskID)) {
            return taskDatabase.epics.get(taskID).getSubtaskReferences();
        }
        return null;
    }

    @Override
    public void deleteAllTasks() {
        for (Integer id: taskDatabase.tasks.keySet()) {
            historyManager.remove(id);
        }
        taskDatabase.tasks.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        for (Epic epic: taskDatabase.epics.values()) {
            epic.clearAllSubtasks();
            epic.updateStatus();
        }
        for (Integer id: taskDatabase.subtasks.keySet()) {
            historyManager.remove(id);
        }
        taskDatabase.subtasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        for (Integer id: taskDatabase.subtasks.keySet()) {
            historyManager.remove(id);
        }
        for (Integer id: taskDatabase.epics.keySet()) {
            historyManager.remove(id);
        }
        taskDatabase.epics.clear();
        taskDatabase.subtasks.clear();
    }

    @Override
    public Task getTaskById(int taskID) {
        historyManager.add(taskDatabase.tasks.get(taskID));
        return taskDatabase.tasks.get(taskID);
    }

    @Override
    public Subtask getSubtaskById(int taskID) {
        historyManager.add(taskDatabase.subtasks.get(taskID));
        return taskDatabase.subtasks.get(taskID);
    }

    @Override
    public Epic getEpicById(int taskID) {
        historyManager.add(taskDatabase.epics.get(taskID));
        return taskDatabase.epics.get(taskID);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return new ArrayList<>(historyManager.getHistory());
    }



}
