package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task implements Comparable<Task> {
    private String title;
    private String description;
    private int taskID;
    private TaskStatus status;
    private Duration duration;
    private LocalDateTime startTime;
    private DateTimeFormatter formatter;

    public Task(String title, String description, TaskStatus status, int duration, String startTime) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.duration = Duration.ofMinutes(duration);
        this.formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        this.startTime = LocalDateTime.parse(startTime, this.formatter);
    }

    public Task(int taskID, String title, String description, TaskStatus status, int duration, String startTime) {
        this(title, description, status, duration, startTime);
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

    @Override
    public int compareTo(Task task) {
        if (this.getStartTime().isBefore(task.getStartTime())) {
            return -1;
        } else if (this.getStartTime().isAfter(task.getStartTime())) {
            return 1;
        } else {
            return 0;
        }
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

    public Duration getDuration() {
        return duration;
    }

    public int getDurationNumeric() {
        return (int) duration.toMinutes();
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public String getStartTimeText() {
        return startTime.format(formatter);
    }

    public LocalDateTime getEndTime() {
        return startTime.plusMinutes(getDurationNumeric());
    }

    public String getEndTimeText() {
        return startTime.plusMinutes(getDurationNumeric()).format(formatter);
    }

    public DateTimeFormatter getFormatter() {
        return formatter;
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

    public void setDuration(int duration) {
        this.duration = Duration.ofMinutes(duration);
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setStartTime(String startTime) {
        this.startTime = LocalDateTime.parse(startTime, formatter);
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setFormatter(DateTimeFormatter newFormatter) {
        this.formatter = newFormatter;
    }

    public String toString() {
        return "model.Task { title= " + title + ",\n description= " + description + ",\n taskID= "
                + taskID + ",\n status=" + status + ",\n start time=" + getStartTimeText() + ",\n duration="
                + getDurationNumeric() + "}\n";
    }

    public String toLine() {
        return String.join(",",
                Integer.toString(getID()),
                TaskType.TASK.toString(),
                getTitle(),
                getStatus().toString(),
                getDescription(),
                getStartTimeText(),
                Integer.toString(getDurationNumeric()),
                "");
    }

    public static Task fromLine(String taskText) {
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
                taskStatus,
                Integer.parseInt(taskFields[6]),
                taskFields[5]
                );
    }

    public boolean intersectsWith(Task task) {
        if ((task.getStartTime().isAfter(this.getEndTime()) &
                task.getEndTime().isAfter(this.getEndTime())) |
                (task.getStartTime().isBefore(this.getStartTime()) &
                        task.getEndTime().isBefore(this.getStartTime()))) {
                return false;
        }
        return true;
    }
}
