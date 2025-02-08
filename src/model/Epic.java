package model;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
     private ArrayList<Subtask> subtaskReferences;

     public Epic(String title, String description) {
         super(title, description, TaskStatus.NEW, 0, "01.01.1900 00:00");
         subtaskReferences = new ArrayList<>();
     }

    public Epic(int taskID, String title, String description) {
        super(taskID, title, description, TaskStatus.NEW, 0, "01.01.1900 00:00");
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

    @Override
    public LocalDateTime getEndTime() {
        return this.getStartTime().plusMinutes(getDurationNumeric());
    }

    @Override
    public String getEndTimeText() {
        return getEndTime().format(getFormatter());
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

    public void updateStartTime() {
        if (subtaskReferences.isEmpty())  {
            this.setStartTime("01.01.1900 00:00");
            return;
        }
        Subtask earliestSubtask = subtaskReferences.getFirst();
        for (Subtask subtask: subtaskReferences) {
            if (subtask.getStartTime().isBefore(earliestSubtask.getStartTime())) {
                earliestSubtask = subtask;
            }
        }
        this.setStartTime(earliestSubtask.getStartTime());
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
                + getID() + ",\n status=" + getStatus() + ",\n start time=" + getStartTimeText() + ",\n duration="
                + getDurationNumeric() + ",\n subtasks=" + subtaskReferences + "}\n";
    }

    public String toLine() {
        return String.join(",",
                Integer.toString(getID()),
                TaskType.EPIC.toString(),
                getTitle(),
                getStatus().toString(),
                getDescription(),
                getStartTimeText(),
                Integer.toString(getDurationNumeric()),
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
