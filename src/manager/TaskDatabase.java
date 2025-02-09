package manager;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

public class TaskDatabase {
    protected Map<Integer, Task> tasks;
    protected Map<Integer, Subtask> subtasks;
    protected Map<Integer, Epic> epics;
    protected Set<Task> prioritizedTasks;

    public TaskDatabase() {
        tasks = new HashMap<>();
        subtasks = new HashMap<>();
        epics = new HashMap<>();
        prioritizedTasks = new TreeSet<>();
    }


}
