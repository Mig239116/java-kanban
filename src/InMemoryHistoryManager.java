import java.util.ArrayList;
import java.util.Iterator;

public class InMemoryHistoryManager implements HistoryManager {
    private ArrayList<Task> tasks;

    public InMemoryHistoryManager() {
        tasks = new ArrayList<>();
    }

    @Override
    public ArrayList<Task> getHistory() {
        return tasks;
    }

    @Override
    public void add(Task task) {
        if (tasks.size()>=10) {
            tasks.remove(0);
        }
        tasks.add(task);
    }

}
