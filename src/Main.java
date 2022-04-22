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
                0, Status.NEW, epic1.getId());

        Subtask subtask1 = manager.createSubtask(newSubtask1);
        Subtask subtask2 = manager.createSubtask(newSubtask2);
        Subtask subtask3 = manager.createSubtask(newSubtask3);

        System.out.println(manager.getTaskById(task1.getId())); //запрашиваем всё, что создали
        System.out.println(manager.getTaskById(task2.getId()));
        System.out.println(manager.getEpicById(epic1.getId()));
        System.out.println(manager.getEpicById(epic2.getId()));
        System.out.println(manager.getSubtaskById(subtask1.getId()));
        System.out.println(manager.getSubtaskById(subtask2.getId()));
        System.out.println(manager.getSubtaskById(subtask3.getId()));
        System.out.println(manager.getHistory()); //выводим историю

        System.out.println(manager.getTaskById(task1.getId())); //проверяем на наличие повторов
        System.out.println(manager.getTaskById(task2.getId()));
        System.out.println(manager.getHistory()); //повторов нет и ранее запрошенные задачи переместились куда нужно!

        manager.removeTaskById(task1.getId()); //удалили задачу 1
        System.out.println(manager.getHistory()); //в истории должно отобразиться всё, кроме задачи 1

        manager.removeEpicById(epic1.getId()); //удалили эпик 1 и, соответственно, его подзадачи
        System.out.println(manager.getHistory()); //теперь в истории остались только эпик 2 и задача 2
    }
}