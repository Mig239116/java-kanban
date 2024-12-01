package manager;

import model.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    private ArrayList<Task> tasks;

    public InMemoryHistoryManager() {
        tasks = new ArrayList<>();
    }

    @Override
    public ArrayList<Task> getHistory() {
        return new ArrayList<>(tasks);
    }

    @Override
    public void add(Task task) {
        if (tasks.size()>=10) {
            tasks.remove(0);
        }
        tasks.add(task);
    }

}
