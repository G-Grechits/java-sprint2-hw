import manager.Managers;
import manager.TaskManager;
import tasks.Status;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

public class Main {

    public static void main(String[] args) {
        System.out.println("Тестовый прогон!");

        TaskManager manager = Managers.getManagerWithCSVSerialization("file.csv");

        Task task1 = manager.createTask(new Task("Task1", "Description task1", 0, Status.NEW));
        Task task2 = manager.createTask(new Task("Task2", "Description task2", 0, Status.NEW));

        Epic epic1 = manager.createEpic(new Epic("Epic1", "Description epic1", 0, Status.NEW));
        Epic epic2 = manager.createEpic(new Epic("Epic2", "Description epic2", 0, Status.NEW));

        Subtask subtask1 = manager.createSubtask(new Subtask("Subtask1", "Description subtask1",
                0, Status.NEW, epic1.getId()));
        Subtask subtask2 = manager.createSubtask(new Subtask("Subtask2", "Description subtask2",
                0, Status.NEW, epic2.getId()));
        Subtask subtask3 = manager.createSubtask(new Subtask("Subtask3", "Description subtask3",
                0, Status.NEW, epic2.getId()));

        manager.getTaskById(task2.getId());
        manager.getEpicById(epic2.getId());
        manager.getSubtaskById(subtask2.getId());
    }
}