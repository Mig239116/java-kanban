import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;

public class Main {

    public static void main(String[] args) {
        Managers manager = new Managers();
        TaskManager taskManager = manager.getDefault();
        taskManager.createTask(new Task(
                "Простая задача 1",
                "Описание простой задачи 1",
                TaskStatus.NEW
                )
        );
        taskManager.createTask(new Task(
                "Простая задача 2",
                "Описание простой задачи 2",
                TaskStatus.IN_PROGRESS
                )
        );
        taskManager.createEpic(new Epic("Эпик 1", "Описание эпика 1"));
        taskManager.createEpic(new Epic("Эпик 2", "Описание эпика 2"));
        taskManager.createSubtask(new Subtask(
                "Подзадача 1",
                "Описание подзадачи 1",
                TaskStatus.NEW,
                3
                )
        );
        taskManager.createSubtask(new Subtask(
                        "Подзадача 2",
                        "Описание подзадачи 2",
                        TaskStatus.NEW,
                        3
                )
        );
        taskManager.createSubtask(new Subtask(
                        "Подзадача 3",
                        "Описание подзадачи 3",
                        TaskStatus.NEW,
                        4
                )
        );
        System.out.println("Создали задачи");
        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());
        taskManager.updateTask(new Task(1,
                        "Простая задача 1",
                        "Описание простой задачи 1",
                        TaskStatus.DONE
                )
        );
        taskManager.updateTask(new Task(2,
                        "Простая задача 15",
                        "Описание простой задачи 15",
                        TaskStatus.DONE
                )
        );
        taskManager.updateSubtask(new Subtask(
                7,
                "Подзадача 3",
                "Описание подзадачи 3",
                TaskStatus.DONE,
                4
            )
        );
        taskManager.updateSubtask(new Subtask(
                        5,
                        "Подзадача 1",
                        "Описание подзадачи 1",
                        TaskStatus.DONE,
                        3
                )
        );
        taskManager.updateSubtask(new Subtask(
                        6,
                        "Подзадача 2",
                        "Описание подзадачи 2",
                        TaskStatus.IN_PROGRESS,
                        3
                )
        );
        printAllTasks(taskManager);

    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getAllTasks()) {
            System.out.println(manager.getTaskById(task.getID()));
        }
        System.out.println("Эпики:");
        for (Task epic : manager.getAllEpics()) {
            System.out.println(manager.getEpicById(epic.getID()));

            for (Task task : manager.getEpicsSubtasks(epic.getID())) {
                System.out.println("--> " + manager.getSubtaskById(task.getID()));
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getAllSubtasks()) {
            System.out.println(manager.getSubtaskById(subtask.getID()));
        }

        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }

}
