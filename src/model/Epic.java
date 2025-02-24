package model;

import com.google.gson.annotations.Expose;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;

public class Epic extends Task {
     @Expose(deserialize = false)
     private ArrayList<Subtask> subtaskReferences;
     @Expose(deserialize = false)
     private LocalDateTime endTime;

     public Epic(String title, String description) {
         super(title, description, TaskStatus.NEW, null, null);
         subtaskReferences = new ArrayList<>();
     }

    public Epic(int taskID, String title, String description) {
        super(taskID, title, description, TaskStatus.NEW, null, null);
        subtaskReferences = new ArrayList<>();
    }


    public ArrayList<Subtask> getSubtaskReferences() {
         return new ArrayList<>(subtaskReferences);
    }

    public Subtask getSubtask(int taskID) {
         for (Subtask subtask: subtaskReferences) {
             if (subtask.getTaskID() == taskID) {
                 return subtask;
             }
         }
         return null;
    }


    public void clearAllSubtasks() {
         subtaskReferences.clear();
    }

    public void addSubtask(Subtask subtask) {
         if (this.getTaskID() != subtask.getTaskID()) {
             subtaskReferences.add(subtask);
         }
    }

    public void deleteSubtask(Subtask subtask) {
         subtaskReferences.remove(subtask);
    }

    public void updateSubtask(Subtask oldSubtask, Subtask newSubtask) {
         subtaskReferences.set(subtaskReferences.indexOf(oldSubtask), newSubtask);
    }

    @Override
    public LocalDateTime getEndTime() {
        return this.endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
         this.endTime = endTime;
    }

    public void intiateSubtasksReferences() {
         this.subtaskReferences = new ArrayList<>();
    }

    @Override
    public String getEndTimeText() {
        return (endTime == null) ? null : endTime.format(getFormatter());
    }

    public void updateDuration() {
        if (subtaskReferences.isEmpty())  {
            this.setDuration(0);
            return;
        }
        int accumulatedDuration = 0;

        for (Subtask subtask: subtaskReferences) {
            accumulatedDuration = accumulatedDuration + subtask.getDurationNumeric();
        }
        this.setDuration(accumulatedDuration);
    }

    public void updateEndTime() {
        if (subtaskReferences.isEmpty())  {
            this.setEndTime(null);
            return;
        }
        Subtask latestSubtask = subtaskReferences.stream()
                .filter(subtask -> subtask.getEndTime() != null)
                .sorted((subtask1, subtask2) -> subtask1.getEndTime()
                        .isBefore(subtask2.getEndTime()) ? -1 : (subtask1.getEndTime()
                        .isAfter(subtask2.getEndTime()) ? 1 : 0))
                .sorted(Comparator.reverseOrder())
                .findFirst()
                .orElse(null);
        if (latestSubtask == null) {
            this.setEndTime(null);
        } else {
            this.setEndTime(latestSubtask.getEndTime());
        }
    }

    public void updateStartTime() {
        if (subtaskReferences.isEmpty())  {
            this.setStartTime(null);
            return;
        }
        Subtask earliestSubtask = subtaskReferences
                .stream()
                .filter(subtask -> subtask.getStartTime() != null)
                .sorted((subtask1, subtask2) -> subtask1.getStartTime()
                        .isBefore(subtask2.getStartTime()) ? -1 : (subtask1.getStartTime()
                        .isAfter(subtask2.getStartTime()) ? 1 : 0))
                .findFirst()
                .orElse(null);
        if (earliestSubtask == null) {
            this.setStartTime(null);
        } else {
            this.setStartTime(earliestSubtask.getStartTime());
        }
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
        return "model.Epic { title= " + getTitle() + ",\n description= " + getDescription() + ",\n taskID= "
                + getTaskID() + ",\n status=" + getStatus() + ",\n start time=" + getStartTimeText() + ",\n duration="
                + getDurationNumeric() + ",\n end time=" + getEndTimeText() + ",\n subtasks=" + subtaskReferences + "}\n";
    }

    public String toLine() {
        return String.join(",",
                Integer.toString(getTaskID()),
                TaskType.EPIC.toString(),
                getTitle(),
                getStatus().toString(),
                getDescription(),
                getStartTimeText(),
                (getDuration() == null) ? Integer.toString(0) : Integer.toString(getDurationNumeric()),
                "");
    }

    public static Epic fromLine(String taskText) {
        String[] taskFields = taskText.split(",");
        return new Epic(Integer.parseInt(taskFields[0]),
                taskFields[2],
                taskFields[4]
        );
    }
}
