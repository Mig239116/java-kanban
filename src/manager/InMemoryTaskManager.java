package manager;

import exceptions.IntersectionException;
import exceptions.NotFoundException;
import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Set;

public class InMemoryTaskManager implements TaskManager {
    protected int tasksCounter;
    protected TaskDatabase taskDatabase;
    protected HistoryManager historyManager;


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

    protected boolean isEpicExists(int taskID) {
        return taskDatabase.epics.containsKey(taskID);
    }

    protected boolean isSubtaskExists(int taskID) {
        return taskDatabase.subtasks.containsKey(taskID);
    }

    protected boolean isSubtaskEpicReferenceValid(Subtask newSubtask) {
        Subtask existingSubtask = taskDatabase.subtasks.get(newSubtask.getTaskID());
        return existingSubtask.getEpicReference() == newSubtask.getEpicReference();
    }

    @Override
    public void createTask(Task task) throws IntersectionException{
        if (!checkIntersections(task)) {
            tasksCounter++;
            task.setTaskID(tasksCounter);
            task.setFormatter(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
            taskDatabase.tasks.put(task.getTaskID(), task);
            if (!ifStartTimeIsNull(task)) {
                addOrDeletePrioritizedTask(task, true);
            }
        } else {
            throw new IntersectionException("Задача пересекается с существующими");
        }
    }

    @Override
    public void createSubtask(Subtask subtask) throws IntersectionException{
        if (this.isEpicExists(subtask.getEpicReference()) & !checkIntersections(subtask)) {
            tasksCounter++;
            subtask.setTaskID(tasksCounter);
            subtask.setFormatter(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
            taskDatabase.subtasks.put(subtask.getTaskID(), subtask);
            Epic currentEpic = taskDatabase.epics.get(subtask.getEpicReference());
            currentEpic.addSubtask(subtask);
            currentEpic.updateStatus();
            currentEpic.updateStartTime();
            currentEpic.updateDuration();
            currentEpic.updateEndTime();
            if (!ifStartTimeIsNull(subtask)) {
                addOrDeletePrioritizedTask(subtask, true);
            }
        }
        if (checkIntersections(subtask)) {
            throw new IntersectionException("Подзадача пересекается с существующими!");
        }
    }



    @Override
    public void createEpic(Epic epic) {
        tasksCounter++;
        epic.setTaskID(tasksCounter);
        epic.setFormatter(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        epic.setStatus(TaskStatus.NEW);
        epic.intiateSubtasksReferences();
        epic.updateDuration();
        epic.updateEndTime();
        epic.updateStartTime();
        taskDatabase.epics.put(epic.getTaskID(), epic);
    }

    @Override
    public void updateTask(Task task) throws IntersectionException, NotFoundException {
        if (taskDatabase.tasks.containsKey(task.getTaskID()) & !checkIntersections(task)) {
            addOrDeletePrioritizedTask(taskDatabase.tasks.get(task.getTaskID()), false);
            taskDatabase.tasks.put(task.getTaskID(), task);
            if (!ifStartTimeIsNull(task)) {
                addOrDeletePrioritizedTask(task, true);
            }
        }
        if (!taskDatabase.tasks.containsKey(task.getTaskID())) {
            throw new NotFoundException("Не найдена данная задача!");
        }
        if (checkIntersections(task)) {
            throw new IntersectionException("Задача пересекается с существующими");
        }
    }

    @Override
    public void updateSubtask(Subtask newSubtask) throws IntersectionException, NotFoundException {
        if (this.isSubtaskExists(newSubtask.getTaskID())
                && this.isSubtaskEpicReferenceValid(newSubtask)
                && !checkIntersections(newSubtask)) {
            Epic currentEpic = taskDatabase.epics.get(newSubtask.getEpicReference());
            Subtask oldSubtask = currentEpic.getSubtask(newSubtask.getTaskID());
            addOrDeletePrioritizedTask(oldSubtask, false);
            currentEpic.updateSubtask(oldSubtask, newSubtask);
            taskDatabase.subtasks.put(newSubtask.getTaskID(), newSubtask);
            currentEpic.updateStatus();
            currentEpic.updateStartTime();
            currentEpic.updateDuration();
            currentEpic.updateEndTime();
            if (!ifStartTimeIsNull(newSubtask)) {
                addOrDeletePrioritizedTask(newSubtask, true);
            }
        }

        if (!this.isSubtaskExists(newSubtask.getTaskID())) {
            throw new NotFoundException("Такой подзадачи не существует");
        }
        if (checkIntersections(newSubtask)) {
            throw new IntersectionException("Подзадача пересекается с существующими!");
        }
    }

    @Override
    public void updateEpic(Epic epic) throws NotFoundException {
        if (this.isEpicExists(epic.getTaskID())) {
            Epic currentEpic = taskDatabase.epics.get(epic.getTaskID());
            currentEpic.setTitle(epic.getTitle());
            currentEpic.setDescription(epic.getDescription());
        } else {
            throw new NotFoundException("Такого эпика не существует!");
        }
    }

    @Override
    public void deleteTaskByID(int taskID) throws NotFoundException {
        if (taskDatabase.tasks.containsKey(taskID)) {
            historyManager.remove(taskID);
            addOrDeletePrioritizedTask(taskDatabase.tasks.get(taskID), false);
            taskDatabase.tasks.remove(taskID);
        } else {
            throw new NotFoundException("Такой задачи не обнаружено!");
        }
    }

    @Override
    public void deleteSubtaskByID(int taskID) throws NotFoundException {
        if (this.isSubtaskExists(taskID)) {
            Subtask subtask = taskDatabase.subtasks.get(taskID);
            Epic currentEpic = taskDatabase.epics.get(subtask.getEpicReference());
            currentEpic.deleteSubtask(subtask);
            currentEpic.updateStatus();
            currentEpic.updateStartTime();
            currentEpic.updateDuration();
            currentEpic.updateEndTime();
            historyManager.remove(taskID);
            addOrDeletePrioritizedTask(taskDatabase.subtasks.get(taskID), false);
            taskDatabase.subtasks.remove(taskID);
        } else {
            throw new NotFoundException("Такой подзадачи не обнаружено!");
        }
    }

    @Override
    public void deleteEpicByID(int taskID) throws NotFoundException {
        if (this.isEpicExists(taskID)) {
            taskDatabase.epics.get(taskID).getSubtaskReferences()
                    .stream()
                    .peek(subtask -> historyManager.remove(subtask.getTaskID()))
                    .peek(subtask -> addOrDeletePrioritizedTask(subtask, false))
                    .forEach(subtask -> taskDatabase.subtasks.remove(subtask.getTaskID()));
            historyManager.remove(taskID);
            taskDatabase.epics.remove(taskID);
        } else {
            throw new NotFoundException("Такого эпика не обнаружено!");
        }
    }

    @Override
    public ArrayList<Subtask> getEpicsSubtasks(int taskID) throws NotFoundException {
        if (this.isEpicExists(taskID)) {
            return taskDatabase.epics.get(taskID).getSubtaskReferences();
        } else {
            throw new NotFoundException("Такого эпика не обнаружено!");
        }
        //return null;
    }

    @Override
    public void deleteAllTasks() {
        taskDatabase.tasks.values()
                        .stream()
                        .peek(task -> historyManager.remove(task.getTaskID()))
                        .forEach(task -> addOrDeletePrioritizedTask(task, false));
        taskDatabase.tasks.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        taskDatabase.epics.values()
                .stream()
                .peek(Epic::clearAllSubtasks)
                .peek(Epic::updateStatus)
                .peek(Epic::updateDuration)
                .peek(Epic::updateStartTime)
                .forEach(Epic::updateEndTime);
        taskDatabase.subtasks.values()
                .stream()
                .peek(subtask -> historyManager.remove(subtask.getTaskID()))
                .forEach(subtask -> addOrDeletePrioritizedTask(subtask, false));
        taskDatabase.subtasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        taskDatabase.subtasks.values()
                .stream()
                .peek(subtask -> historyManager.remove(subtask.getTaskID()))
                .forEach(subtask -> addOrDeletePrioritizedTask(subtask, false));
        for (Integer id: taskDatabase.epics.keySet()) {
            historyManager.remove(id);
        }
        taskDatabase.epics.clear();
        taskDatabase.subtasks.clear();
    }

    @Override
    public Task getTaskById(int taskID) throws NotFoundException {
        if (taskDatabase.tasks.get(taskID) != null) {
            historyManager.add(taskDatabase.tasks.get(taskID));
            return taskDatabase.tasks.get(taskID);
        } else {
            throw new NotFoundException("Такой задачи не обнаружено!");
        }
    }

    @Override
    public Subtask getSubtaskById(int taskID) throws NotFoundException{
        if (taskDatabase.subtasks.get(taskID) != null) {
            historyManager.add(taskDatabase.subtasks.get(taskID));
            return taskDatabase.subtasks.get(taskID);
        } else {
            throw new NotFoundException("Такой подзадачи не обнаружено!");
        }
    }

    @Override
    public Epic getEpicById(int taskID) throws NotFoundException{
        if (taskDatabase.epics.get(taskID) != null) {
            historyManager.add(taskDatabase.epics.get(taskID));
            return taskDatabase.epics.get(taskID);
        } else {
            throw new NotFoundException("Такого эпика не обнаружено!");
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return new ArrayList<>(historyManager.getHistory());
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        return taskDatabase.prioritizedTasks;
    }


    public boolean checkIntersections(Task task) {
        return getPrioritizedTasks()
                .stream()
                .filter(prioritizedTask -> prioritizedTask.getTaskID() != task.getTaskID())
                .anyMatch(prioritizedTask -> prioritizedTask.intersectsWith(task));
    }


    public boolean ifStartTimeIsNull(Task task) {
        return task.getStartTime() == null;
    }

    public void addOrDeletePrioritizedTask(Task task, boolean creation) {
        if (creation) {
            taskDatabase.prioritizedTasks.add(task);
        } else {
            taskDatabase.prioritizedTasks.remove(task);
        }
    }

}
