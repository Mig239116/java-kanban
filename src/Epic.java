import java.util.ArrayList;

public class Epic extends Task {
     private ArrayList<Subtask> subtaskReferences;

     public Epic(String title, String description) {
         super(title, description, TaskStatus.NEW);
         subtaskReferences = new ArrayList<>();
     }

    public Epic(int taskID, String title, String description) {
        super(taskID, title, description, TaskStatus.NEW);
        subtaskReferences = new ArrayList<>();
    }


    public ArrayList<Subtask> getSubtaskReferences() {
         return new ArrayList<>(subtaskReferences);
    }

    public Subtask getSubtask(int taskID) {
         for (Subtask subtask: subtaskReferences) {
             if (subtask.getID() == taskID) {
                 return subtask;
             }
         }
         return null;
    }

    public void clearAllSubtasks() {
         subtaskReferences.clear();
    }

    public void addSubtask(Subtask subtask) {
         if (this.getID() != subtask.getID()) {
             subtaskReferences.add(subtask);
         }
    }

    public void deleteSubtask(Subtask subtask) {
         subtaskReferences.remove(subtask);
    }

    public void updateSubtask(Subtask oldSubtask, Subtask newSubtask) {
         subtaskReferences.set(subtaskReferences.indexOf(oldSubtask), newSubtask);
    }

    public void updateStatus() {
         if (subtaskReferences.isEmpty())  {
             this.setStatus(TaskStatus.NEW);
             return;
         }
         int counterNew = 0;
         int counterDone = 0;
         for (Subtask subtask: subtaskReferences) {
             if (subtask.getStatus() == TaskStatus.DONE) {
                 counterDone++;
             } else if (subtask.getStatus() == TaskStatus.NEW) {
                 counterNew++;
             }
         }
         if (subtaskReferences.size() == counterNew) {
             this.setStatus(TaskStatus.NEW);
             return;
         } else if (subtaskReferences.size() == counterDone) {
             this.setStatus(TaskStatus.DONE);
             return;
         }
        this.setStatus(TaskStatus.IN_PROGRESS);
    }

    public String toString() {
        return "Epic { title= " + getTitle() + ",\n description= " + getDescription() + ",\n taskID= "
                + getID() + ",\n status=" + getStatus() + ",\n subtasks=" + subtaskReferences + "}\n";
    }
}
