package manager;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.Map;
import java.util.HashMap;

public class TaskDatabase {
    protected Map<Integer, Task> tasks;
    protected Map<Integer, Subtask> subtasks;
    protected Map<Integer, Epic> epics;

    public TaskDatabase(){
        tasks = new HashMap<>();
        subtasks = new HashMap<>();
        epics = new HashMap<>();
    }
}
