import manager.Managers;
import manager.TaskManager;
import tasks.Status;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

public class Main {

    public static void main(String[] args) {
        System.out.println("Тестовый прогон!"); //взял в качестве примера модель тестирования из ТЗ

        TaskManager manager = Managers.getDefault();
        Task newTask1 = new Task("Название задачи 1", "описание задачи 1", 0, Status.NEW);
        Task newTask2 = new Task("Название задачи 2", "описание задачи 2", 0, Status.NEW);

        Task task1 = manager.createTask(newTask1);
        Task task2 = manager.createTask(newTask2);

        Epic newEpic1 = new Epic("Название эпика 1", "описание эпика 1", 0, Status.NEW);
        Epic newEpic2 = new Epic("Название эпика 2", "описание эпика 2", 0, Status.NEW);

        Epic epic1 = manager.createEpic(newEpic1);
        Epic epic2 = manager.createEpic(newEpic2);

        Subtask newSubtask1 = new Subtask("Название подзадачи 1", "описание подзадачи 1",
                0, Status.NEW, epic1.getId());
        Subtask newSubtask2 = new Subtask("Название подзадачи 2", "описание подзадачи 2",
                0, Status.NEW, epic1.getId());
        Subtask newSubtask3 = new Subtask("Название подзадачи 3", "описание подзадачи 3",
                0, Status.NEW, epic2.getId());

        Subtask subtask1 = manager.createSubtask(newSubtask1);
        Subtask subtask2 = manager.createSubtask(newSubtask2);
        Subtask subtask3 = manager.createSubtask(newSubtask3);

        System.out.println(manager.getTaskById(task1.getId()));
        System.out.println(manager.getHistory());
        System.out.println(manager.getEpicById(epic1.getId()));
        System.out.println(manager.getHistory());
        System.out.println(manager.getSubtaskById(subtask1.getId()));
        System.out.println(manager.getSubtaskById(subtask2.getId()));
        System.out.println(manager.getSubtaskById(subtask3.getId()));
        System.out.println(manager.getHistory());
        System.out.println(manager.getTaskById(task2.getId())); //проверяем обозначенное ограничение по хранению
        System.out.println(manager.getSubtaskById(subtask1.getId()));
        System.out.println(manager.getSubtaskById(subtask2.getId()));
        System.out.println(manager.getSubtaskById(subtask3.getId()));
        System.out.println(manager.getTaskById(task1.getId()));
        System.out.println(manager.getTaskById(task2.getId()));
        System.out.println(manager.getTaskById(task1.getId()));
        System.out.println(manager.getHistory()); //должен вывести только задачи и подзадачи

//        System.out.println(manager.getAllTasks());
//        System.out.println(manager.getAllEpics());
//        System.out.println(manager.getAllSubtasks());
//        System.out.println("Подзадачи эпика с ID=" + epic1.getId() + ": " + manager.getSubtasksByEpic(epic1.getId()));
//
//        Subtask newSubtask4 = new Subtask("Название подзадачи 4", "описание подзадачи 4",
//                subtask1.getId(), Status.DONE, epic1.getId());
//        Subtask newSubtask5 = new Subtask("Название подзадачи 5", "описание подзадачи 5",
//                subtask3.getId(), Status.DONE, epic2.getId());
//
//        manager.updateSubtask(newSubtask4);
//        manager.updateSubtask(newSubtask5);
//
//        System.out.println(manager.getAllTasks());
//        System.out.println(manager.getAllEpics());
//        System.out.println(manager.getAllSubtasks());
//
//        manager.removeTaskById(task1.getId());
//        manager.removeEpicById(epic1.getId());
//
//        System.out.println(manager.getAllTasks());
//        System.out.println(manager.getAllEpics());
//        System.out.println(manager.getAllSubtasks());
//
//        manager.removeSubtaskById(newSubtask5.getId());
//
//        System.out.println(manager.getAllTasks());
//        System.out.println(manager.getAllEpics());
//        System.out.println(manager.getAllSubtasks());
    }
}