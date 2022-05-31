import manager.FileBackedTaskManager;
import manager.Managers;
import manager.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.time.LocalDateTime;
import java.time.Month;

public class Main {

    public static void main(String[] args) {
        System.out.println("Тестовый прогон!");

        String fileName = "file1.csv";
        TaskManager manager = Managers.getManagerWithCSVSerialization("resources/" + fileName);

        Task task1 = manager.createTask(new Task("Task1", "Task1 description", 0, Status.NEW,
                60L, LocalDateTime.of(2022, Month.JUNE, 1, 12, 0)));
        Task task2 = manager.createTask(new Task("Task2", "Task2 description", 0, Status.NEW,
                80L, LocalDateTime.of(2022, Month.JUNE, 2, 14, 0)));

        Epic epic1 = manager.createEpic(new Epic("Epic1", "Epic1 description", 0, Status.NEW));
        Epic epic2 = manager.createEpic(new Epic("Epic2", "Epic2 description", 0, Status.NEW));

        Subtask subtask1 = manager.createSubtask(new Subtask("Subtask1", "Subtask1 description", 0,
                Status.NEW, 30L, LocalDateTime.of(2022, Month.JUNE, 3, 11, 0),
                epic1.getId()));
        Subtask subtask2 = manager.createSubtask(new Subtask("Subtask2", "Subtask2 description", 0,
                Status.DONE, 40L, LocalDateTime.of(2022, Month.JUNE, 1, 17, 0),
                epic2.getId()));
        Subtask subtask3 = manager.createSubtask(new Subtask("Subtask3", "Subtask3 description", 0,
                Status.NEW, 30L, LocalDateTime.of(2022, Month.JUNE, 2, 16, 0),
                epic2.getId()));

        manager.getTaskById(task2.getId());
        manager.getEpicById(epic2.getId());
        manager.getSubtaskById(subtask1.getId());
        manager.getSubtaskById(subtask2.getId());

        System.out.println("Старая история:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
        TaskManager loadManager = FileBackedTaskManager.loadFromFile("resources/" + fileName);
        System.out.println("Загруженная история:");
        for (Task task : loadManager.getHistory()) {
            System.out.println(task);
        }
    }
}