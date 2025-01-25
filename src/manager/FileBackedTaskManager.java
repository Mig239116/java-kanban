package manager;

import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskType;

import java.io.*;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {

private File autoSaveFile;
    final String fieldsNames = "id,type,name,status,description,epic";

    public FileBackedTaskManager(String fileFullPath) {
        super();
        autoSaveFile = new File(fileFullPath);
    }

    public File getAutoSaveFile() {
        return autoSaveFile;
    }

    public void save() {
        if (taskDatabase.tasks.isEmpty() &
                taskDatabase.subtasks.isEmpty() &
                taskDatabase.epics.isEmpty()
        ) {
            return;
        }
        try(BufferedWriter bufferedWriter =
                    new BufferedWriter(new FileWriter(autoSaveFile.toString()))) {
            bufferedWriter.write(fieldsNames);
            bufferedWriter.newLine();
            for (Task task: taskDatabase.tasks.values()) {
                bufferedWriter.write(task.toLine());
                bufferedWriter.newLine();
            }
            for (Epic epic: taskDatabase.epics.values()) {
                bufferedWriter.write(epic.toLine());
                bufferedWriter.newLine();
            }
            for (Subtask subtask: taskDatabase.subtasks.values()) {
                bufferedWriter.write(subtask.toLine());
                bufferedWriter.newLine();
            }
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка загрузки в файл " + autoSaveFile.getName());
        }
    }

    public static  FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file.toString());
        String workingLine;
        if (file.length() == 0 || !file.exists()) return null;
        try (BufferedReader bufferedReader =
                     new BufferedReader(new FileReader(file.toString()))) {
            bufferedReader.readLine();
            fileBackedTaskManager.tasksCounter = 0;
            while (bufferedReader.ready()) {
                workingLine = bufferedReader.readLine();
                if (workingLine.contains(TaskType.SUBTASK.toString())) {
                    Subtask subtask = Subtask.fromLine(workingLine);
                    if (fileBackedTaskManager.tasksCounter < subtask.getID()) {
                        fileBackedTaskManager.tasksCounter = subtask.getID();
                    }
                        fileBackedTaskManager.taskDatabase.subtasks.put(subtask.getID(),
                                subtask);
                } else if (workingLine.contains(TaskType.EPIC.toString())) {
                    Epic epic = Epic.fromLine(workingLine);
                    if (fileBackedTaskManager.tasksCounter < epic.getID()) {
                        fileBackedTaskManager.tasksCounter = epic.getID();
                    }
                    fileBackedTaskManager.taskDatabase.epics.put(epic.getID(),
                            epic);
                } else {
                    Task task = Task.fromLine(workingLine);
                    if (fileBackedTaskManager.tasksCounter < task.getID()) {
                        fileBackedTaskManager.tasksCounter = task.getID();
                    }
                    fileBackedTaskManager.taskDatabase.tasks.put(task.getID(),
                            task);
                }

            }

                for (Subtask subtask: fileBackedTaskManager.taskDatabase.subtasks.values()) {
                    if (fileBackedTaskManager.isEpicExists(subtask.getEpicReference())) {
                        Epic currentEpic = fileBackedTaskManager.taskDatabase.epics.get(subtask.getEpicReference());
                        currentEpic.addSubtask(subtask);
                        currentEpic.updateStatus();
                    } else {
                        fileBackedTaskManager.taskDatabase.subtasks.remove(subtask.getID());
                    }
                }
        } catch (IOException exception) {
            throw new ManagerLoadException();
        }
        return fileBackedTaskManager;
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubtask(Subtask newSubtask) {
        super.updateSubtask(newSubtask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void deleteTaskByID(int taskID) {
        super.deleteTaskByID(taskID);
        save();
    }

    @Override
    public void deleteSubtaskByID(int taskID) {
        super.deleteSubtaskByID(taskID);
        save();
    }

    @Override
    public void deleteEpicByID(int taskID) {
        super.deleteEpicByID(taskID);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

}
