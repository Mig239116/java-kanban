package model;

public class Task {
    private String title;
    private String description;
    private int taskID;
    private TaskStatus status;

    public Task(String title, String description, TaskStatus status) {
        this.title = title;
        this.description = description;
        this.status = status;
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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String toString() {
        return "model.Task { title= " + title + ",\n description= " + description + ",\n taskID= "
                + taskID + ",\n status=" + status + "}\n";
    }

    public String toLine() {
        return String.join(",",
                Integer.toString(getID()),
                TaskType.TASK.toString(),
                getTitle(),
                getStatus().toString(),
                getDescription(),
                "");
    }

    static public Task fromLine(String taskText) {
        String[] taskFields = taskText.split(",");
        TaskStatus taskStatus;
        switch (taskFields[3]) {
            case "NEW":
                taskStatus = TaskStatus.NEW;
                break;
            case "DONE":
                taskStatus = TaskStatus.DONE;
                break;
            default:
                taskStatus = TaskStatus.IN_PROGRESS;
                break;
        }
        return new Task(Integer.parseInt(taskFields[0]),
                taskFields[2],
                taskFields[4],
                taskStatus
                );
    }
}
