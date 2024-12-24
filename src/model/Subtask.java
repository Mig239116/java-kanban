package model;

public class Subtask extends Task {
    private Integer epicReference;

    public Subtask(String title, String description, TaskStatus status, Integer epicReference) {
        super(title, description, status);
        this.epicReference = epicReference;
    }

    public Subtask(int taskID, String title, String description, TaskStatus status, Integer epicReference) {
        super(taskID, title, description, status);
        this.epicReference = epicReference;
    }

    public int getEpicReference() {
        return epicReference;
    }

    public String toString() {
        return "model.Subtask { title= " + getTitle() + ",\n description= " + getDescription() + ",\n taskID= "
                + getID() + ",\n status=" + getStatus() + ",\n epicRef=" + epicReference + "}\n";
    }
}
