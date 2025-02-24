import manager.FileBackedTaskManager;
import manager.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;

import java.io.File;

public class Main {

    public static void main(String[] args) {

        FileBackedTaskManager taskManager =
                new FileBackedTaskManager(
                System.getProperty("user.home") + "/checkFile.csv");
        createAllTasks(taskManager);
        File sourceFile = new File(System.getProperty("user.home") + "/checkFile.csv");
        FileBackedTaskManager taskManager1 = FileBackedTaskManager.loadFromFile(sourceFile);
        taskManager1.createTask(new Task(
                        "Проверка счетчика задач",
                        "Должен быть больше максимального ID",
                        TaskStatus.NEW,
                        30,
                        "08.02.2024 13:47"
                )
        );
        printAllTasks(taskManager1);
    }

    private static void createAllTasks(TaskManager taskManager) {
        taskManager.createTask(new Task(
                        "Простая задача 1",
                        "Описание простой задачи 1",
                        TaskStatus.NEW,
                        10,
                        "04.05.1983 12:00"
                )
        );
        taskManager.createTask(new Task(
                        "Простая задача 2",
                        "Описание простой задачи 2",
                        TaskStatus.IN_PROGRESS,
                        150,
                        "11.03.2025 14:25"
                )
        );
        taskManager.createEpic(new Epic("Эпик 1", "Описание эпика 1"));
        taskManager.createEpic(new Epic("Эпик 2", "Описание эпика 2"));
        taskManager.createSubtask(new Subtask(
                        "Подзадача 1",
                        "Описание подзадачи 1",
                        TaskStatus.NEW,
                        90,
                "11.03.2028 14:25",
                        3
                )
        );
        taskManager.createSubtask(new Subtask(
                        "Подзадача 2",
                        "Описание подзадачи 2",
                        TaskStatus.NEW,
                10,
                "10.03.2025 14:25",
                        3
                )
        );
        taskManager.createSubtask(new Subtask(
                        "Подзадача 3",
                        "Описание подзадачи 3",
                        TaskStatus.NEW,
                35,
                "11.04.2025 14:25",
                        4
                )
        );
        taskManager.updateTask(new Task(1,
                        "Простая задача 1",
                        "Описание простой задачи 1",
                        TaskStatus.DONE,
                        21,
                        "13.01.2026 13:47"
                )
        );
        taskManager.updateTask(new Task(2,
                        "Простая задача 15",
                        "Описание простой задачи 15",
                        TaskStatus.DONE,
                137,
                "13.01.2025 13:50"
                )
        );
        taskManager.updateSubtask(new Subtask(
                        7,
                        "Подзадача 3",
                        "Описание подзадачи 3",
                        TaskStatus.DONE,
                35,
                "11.04.2025 14:25",
                        4
                )
        );
        taskManager.updateSubtask(new Subtask(
                        5,
                        "Подзадача 1",
                        "Описание подзадачи 1",
                        TaskStatus.DONE,
                10,
                "10.03.2025 14:25",
                        3
                )
        );
        taskManager.updateSubtask(new Subtask(
                        6,
                        "Подзадача 2",
                        "Описание подзадачи 2",
                        TaskStatus.IN_PROGRESS,
                90,
                "11.03.2025 14:25",
                        3
                )
        );
        System.out.println("Приоритеты:");
        System.out.println(taskManager.getPrioritizedTasks());
    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getAllTasks()) {
            System.out.println(manager.getTaskById(task.getID()));
        }
        System.out.println("Эпики:");
        for (Task epic : manager.getAllEpics()) {
            System.out.println(manager.getEpicById(epic.getID()));
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getAllSubtasks()) {
            System.out.println(manager.getSubtaskById(subtask.getID()));
        }
        System.out.println("Приоритеты:");
        System.out.println(manager.getPrioritizedTasks());

    }

}
