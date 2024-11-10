public class Task {
    private String title;
    private String description;
    private int taskID;
    private TaskStatus status;

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.status = TaskStatus.NEW;
    }

    public Task(int taskID, String title, String description, TaskStatus status) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.taskID = taskID;
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        Task task = (Task) obj;
        return task.taskID == this.taskID;
    }

    public int hashCode() {
        return taskID;
    }

    public int getID() {
        return taskID;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setID(int taskID) {
        this.taskID = taskID;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public String toString() {
        return "Task { title= " + title + ",\n description= " + description + ",\n taskID= "
                + taskID + ",\n status=" + status + "}\n";
    }
}
