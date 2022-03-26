public class Main {

    public static void main(String[] args) {
        System.out.println("Тестовый прогон!"); //взял в качестве примера модель тестирования из ТЗ

        Manager manager = new Manager();
        Task newTask1 = new Task("Название задачи 1", "описание задачи 1", 0, Status.newStatus);
        Task newTask2 = new Task("Название задачи 2", "описание задачи 2", 0, Status.newStatus);
        Epic newEpic1 = new Epic("Название эпика 1", "описание эпика 1", 0, Status.newStatus);
        Epic newEpic2 = new Epic("Название эпика 2", "описание эпика 2", 0, Status.newStatus);
        Subtask newSubtask1 = new Subtask("Название подзадачи 1", "описание подзадачи 1",
                0, Status.newStatus, 3);
        Subtask newSubtask2 = new Subtask("Название подзадачи 2", "описание подзадачи 2",
                0, Status.newStatus, 3);
        Subtask newSubtask3 = new Subtask("Название подзадачи 3", "описание подзадачи 3",
                0, Status.newStatus, 6);
        Subtask newSubtask4 = new Subtask("Название подзадачи 4", "описание подзадачи 4",
                4, Status.done, 3);
        Subtask newSubtask5 = new Subtask("Название подзадачи 5", "описание подзадачи 5",
                7, Status.done, 6);

        Task task1 = manager.createTask(newTask1);
        Task task2 = manager.createTask(newTask2);
        Epic epic1 = manager.createEpic(newEpic1);
        Subtask subtask1 = manager.createSubtask(newSubtask1);
        Subtask subtask2 = manager.createSubtask(newSubtask2);
        Epic epic2 = manager.createEpic(newEpic2);
        Subtask subtask3 = manager.createSubtask(newSubtask3);

        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubtasks());

        manager.updateSubtask(newSubtask4);
        manager.updateSubtask(newSubtask5);

        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubtasks());

        manager.removeTaskById(1);
        manager.removeEpicById(3);

        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubtasks());
    }
}