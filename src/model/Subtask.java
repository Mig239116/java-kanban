package model;

public class Subtask extends Task {
    private Integer epicReference;

    public Subtask(String title, String description, TaskStatus status,
                   int duration, String startTime, Integer epicReference) {
        super(title, description, status, duration, startTime);
        this.epicReference = epicReference;
    }

    public Subtask(int taskID, String title, String description, TaskStatus status,
                   int duration, String startTime, Integer epicReference) {
        super(taskID, title, description, status, duration, startTime);
        this.epicReference = epicReference;
    }

    public int getEpicReference() {
        return epicReference;
    }

    public String toString() {
        return "model.Subtask { title= " + getTitle() + ",\n description= " + getDescription() + ",\n taskID= "
                + getID() + ",\n status=" + getStatus() + ",\n start time=" + getStartTimeText() + ",\n duration="
                + getDurationNumeric() + ",\n epicRef=" + epicReference + "}\n";
    }

    public String toLine() {
        return String.join(",",
                Integer.toString(getID()),
                TaskType.SUBTASK.toString(),
                getTitle(),
                getStatus().toString(),
                getDescription(),
                getStartTimeText(),
                Integer.toString(getDurationNumeric()),
                Integer.toString(getEpicReference()));
    }

    public static Subtask fromLine(String taskText) {
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
        return new Subtask(Integer.parseInt(taskFields[0]),
                taskFields[2],
                taskFields[4],
                taskStatus,
                Integer.parseInt(taskFields[6]),
                taskFields[5],
                Integer.parseInt((taskFields[7]))
        );
    }
}
