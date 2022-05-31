package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    protected T manager;
    Task task;
    Epic epic;
    Subtask subtask;

    @BeforeEach
    void initializeAttributes() {
        task = manager.createTask(new Task("Task1", "Task1 description", 0, Status.NEW,
                60L, LocalDateTime.of(2022, Month.JUNE, 1, 12, 0)));
        epic = manager.createEpic(new Epic("Epic1", "Epic1 description", 0, Status.NEW));
        subtask = manager.createSubtask(new Subtask("Subtask1", "Subtask1 description", 0,
                Status.NEW, 30L, LocalDateTime.of(2022, Month.JUNE, 2, 11, 0),
                epic.getId()));
    }

    @Test
    void getHistoryIfTasksWereNotGetting() {
        assertEquals(0, manager.getHistory().size(), "История просмотров не пустая.");
    }

    @Test
    void getHistoryWith2Tasks() {
        manager.getTaskById(task.getId());
        manager.getSubtaskById(subtask.getId());

        assertEquals(2, manager.getHistory().size(), "История просмотров содержит" +
                "неверное количество задач.");
    }

    @Test
    void getHistoryWithDuplication() {
        manager.getTaskById(task.getId());
        manager.getSubtaskById(subtask.getId());
        manager.getTaskById(task.getId());

        assertEquals(2, manager.getHistory().size(), "История просмотров содержит" +
                "неверное количество задач.");
    }

    @Test
    void getHistoryIfFirstTaskIsRemoved() {
        manager.getTaskById(task.getId());
        manager.getEpicById(epic.getId());
        manager.getSubtaskById(subtask.getId());
        manager.removeTaskById(task.getId());

        assertEquals(epic, manager.getHistory().get(0), "Метод удаления задач из истории просмотров" +
                "работает некорректно.");
        assertEquals(subtask, manager.getHistory().get(1), "Метод удаления задач из истории просмотров" +
                "работает некорректно.");
    }

    @Test
    void getHistoryIfTaskFromMiddleIsRemoved() {
        manager.getTaskById(task.getId());
        manager.getSubtaskById(subtask.getId());
        manager.getEpicById(epic.getId());
        manager.removeSubtaskById(subtask.getId());

        assertEquals(task, manager.getHistory().get(0), "Метод удаления задач из истории просмотров" +
                "работает некорректно.");
        assertEquals(epic, manager.getHistory().get(1), "Метод удаления задач из истории просмотров" +
                "работает некорректно.");
    }

    @Test
    void getHistoryIfLastTaskIsRemoved() {
        manager.getTaskById(task.getId());
        manager.getEpicById(epic.getId());
        manager.getSubtaskById(subtask.getId());
        manager.removeSubtaskById(subtask.getId());

        assertEquals(task, manager.getHistory().get(0), "Метод удаления задач из истории просмотров" +
                "работает некорректно.");
        assertEquals(epic, manager.getHistory().get(1), "Метод удаления задач из истории просмотров" +
                "работает некорректно.");
    }

    @Test
    void getAllTasks() {
        List<Task> tasks = manager.getAllTasks();

        assertNotNull(tasks, "Список задач не возвращается.");
        assertEquals(1, tasks.size(), "Неверное количество задач в списке.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void getAllEpics() {
        List<Epic> epics = manager.getAllEpics();

        assertNotNull(epics, "Список эпиков не возвращается.");
        assertEquals(1, epics.size(), "Неверное количество эпиков в списке.");
        assertEquals(epic, epics.get(0), "Эпики не совпадают.");
    }

    @Test
    void getAllSubtasks() {
        List<Subtask> subtasks = manager.getAllSubtasks();

        assertNotNull(subtasks, "Список подзадач не возвращается.");
        assertEquals(1, subtasks.size(), "Неверное количество подзадач в списке.");
        assertEquals(subtask, subtasks.get(0), "Подзадачи не совпадают.");
    }

    @Test
    void removeAllTasks() {
        manager.removeAllTasks();
        List<Task> tasks = manager.getAllTasks();

        assertEquals(0, tasks.size(), "Список задач не очищается.");
    }

    @Test
    void removeAllEpics() {
        manager.removeAllEpics();
        List<Epic> epics = manager.getAllEpics();
        List<Subtask> subtasks = manager.getAllSubtasks();

        assertEquals(0, epics.size(), "Список эпиков не очищается.");
        assertEquals(0, subtasks.size(), "Список подзадач не очищается.");
    }

    @Test
    void removeAllSubtasks() {
        manager.removeAllSubtasks();
        List<Subtask> subtasks = manager.getAllSubtasks();

        assertEquals(0, subtasks.size(), "Список подзадач не очищается.");
    }

    @Test
    void getTaskById() {
        Task newTask = manager.getTaskById(task.getId());

        assertNotNull(newTask, "Задача не возвращается.");
        assertEquals(task, newTask, "Задачи не совпадают.");
    }

    @Test
    void getTaskByWrongId() {
        Task newTask = manager.getTaskById(4);

        assertNull(newTask, "Возвращается задача, а не null.");
    }

    @Test
    void getEpicById() {
        Epic newEpic = manager.getEpicById(epic.getId());

        assertNotNull(newEpic, "Эпик не возвращается.");
        assertEquals(epic, newEpic, "Эпики не совпадают.");
    }

    @Test
    void getEpicByWrongId() {
        Epic newEpic = manager.getEpicById(4);

        assertNull(newEpic, "Возвращается эпик, а не null.");
    }

    @Test
    void getSubtaskById() {
        Subtask newSubtask = manager.getSubtaskById(subtask.getId());

        assertNotNull(newSubtask, "Подзадача не возвращается.");
        assertEquals(subtask, newSubtask, "Подзадачи не совпадают.");
        assertEquals(2, newSubtask.getEpicId(), "Эпик подзадачи определяется некорректно.");
    }

    @Test
    void getSubtaskByWrongId() {
        Subtask newSubtask = manager.getSubtaskById(4);

        assertNull(newSubtask, "Возвращается подзадача, а не null.");
    }

    @Test
    void removeTaskById() {
        manager.removeTaskById(task.getId());
        Task newTask = manager.getTaskById(task.getId());

        assertNull(newTask, "Задача не удаляется");
    }

    @Test
    void removeTaskByWrongId() {
        manager.removeTaskById(4);

        assertEquals(1, manager.getAllTasks().size(), "Неверное количество задач в списке.");
        assertTrue(manager.getAllTasks().contains(task), "Задачи нет в списке.");
    }

    @Test
    void removeEpicById() {
        manager.removeEpicById(epic.getId());
        Epic newEpic = manager.getEpicById(epic.getId());

        assertNull(newEpic, "Эпик не удаляется.");
        assertEquals(0, manager.getAllSubtasks().size(), "Подзадачи эпика не удаляются.");
    }

    @Test
    void removeEpicByWrongId() {
        manager.removeEpicById(4);

        assertEquals(1, manager.getAllEpics().size(), "Неверное количество эпиков в списке.");
        assertTrue(manager.getAllEpics().contains(epic), "Эпика нет в списке.");
    }


    @Test
    void removeSubtaskById() {
        manager.removeSubtaskById(subtask.getId());
        Subtask newSubtask = manager.getSubtaskById(subtask.getId());

        assertNull(newSubtask, "Подзадача не удаляется");
    }

    @Test
    void removeSubtaskByWrongId() {
        manager.removeSubtaskById(4);

        assertEquals(1, manager.getAllSubtasks().size(), "Неверное количество подзадач в списке.");
        assertTrue(manager.getAllSubtasks().contains(subtask), "Подзадачи нет в списке.");
    }

    @Test
    void createTask() {
        Task newTask = manager.getTaskById(1);

        assertEquals(task, newTask, "Задачи не совпадают. Возможно, сгенерировался некорректный ID.");
    }

    @Test
    void createEpic() {
        Epic newEpic = manager.getEpicById(2);

        assertEquals(epic, newEpic, "Эпики не совпадают. Возможно, сгенерировался некорректный ID.");
    }

    @Test
    void createSubtask() {
        Subtask newSubtask = manager.getSubtaskById(3);

        assertEquals(subtask, newSubtask, "Подзадачи не совпадают. Возможно, сгенерировался некорректный ID.");
        assertEquals(2, newSubtask.getEpicId(), "Эпик подзадачи определяется некорректно.");
    }

    @Test
    void updateTask() {
        Task newTask = new Task("Task2", "Task2 description", task.getId(), Status.NEW,
                60L, LocalDateTime.of(2022, Month.JUNE, 1, 12, 0));
        manager.updateTask(newTask);

        assertEquals(1, manager.getAllTasks().size(), "Количество задач в списке изменяется.");
        assertEquals(newTask, manager.getAllTasks().get(0), "Задача не обновляется.");
    }

    @Test
    void updateEpic() {
        Epic newEpic = new Epic("Epic2", "Epic2 description", epic.getId(), Status.NEW);
        manager.updateEpic(newEpic);

        assertEquals(1, manager.getAllEpics().size(), "Количество эпиков в списке изменяется.");
        assertEquals(newEpic, manager.getAllEpics().get(0), "Эпик не обновляется.");
    }

    @Test
    void updateSubtask() {
        Subtask newSubtask = new Subtask("Subtask2", "Subtask2 description", subtask.getId(), Status.NEW,
                30L, LocalDateTime.of(2022, Month.JUNE, 2, 11, 0), epic.getId());
        manager.updateSubtask(newSubtask);

        assertEquals(1, manager.getAllSubtasks().size(), "Количество подзадач в списке изменяется.");
        assertEquals(newSubtask, manager.getAllSubtasks().get(0), "Подзадача не обновляется.");
        assertEquals(2, newSubtask.getEpicId(), "Эпик подзадачи определяется некорректно.");
    }

    @Test
    void getSubtasksByEpic() {
        List<Subtask> subtasksOfEpic = manager.getSubtasksByEpic(epic.getId());

        assertNotNull(subtasksOfEpic, "Список подзадач эпика не возвращается.");
        assertEquals(1, subtasksOfEpic.size(), "Неверное количество подзадач в эпике.");
        assertEquals(subtask, subtasksOfEpic.get(0), "Подзадачи нет в эпике.");
    }

    @Test
    void getSubtasksByWrongEpic() {
        List<Subtask> subtasksOfEpic = manager.getSubtasksByEpic(4);

        assertNull(subtasksOfEpic, "Возвращается подзадача, а не null.");
    }

    @Test
    void updateStatusOfEpicWhenSubtaskListIsEmpty() {
        Epic newEpic = manager.createEpic(new Epic("Epic2", "Epic2 description", 0, Status.DONE));

        assertEquals(2, manager.getAllEpics().size(), "Неверное количество эпиков в списке.");
        assertEquals(newEpic, manager.getAllEpics().get(1), "Эпики не совпадают.");
        assertTrue(newEpic.getSubtasks().isEmpty(), "Список подзадач нового эпика не пустой.");
        assertEquals(Status.NEW, newEpic.getStatus(), "Статус нового эпика не NEW.");
    }

    @Test
    void updateStatusOfEpicWhenAllSubtasksAreNew() {
        Epic newEpic = manager.createEpic(new Epic("Epic2", "Epic2 description", 0, Status.DONE));
        Subtask newSubtask1 = manager.createSubtask(new Subtask("Subtask2", "Subtask2 description",
                0, Status.NEW, 30L, LocalDateTime.of(2022, Month.JUNE, 2, 13, 0),
                newEpic.getId()));
        Subtask newSubtask2 = manager.createSubtask(new Subtask("Subtask3", "Subtask3 description",
                0, Status.NEW, 30L, LocalDateTime.of(2022, Month.JUNE, 2, 14, 0),
                newEpic.getId()));

        assertEquals(2, newEpic.getSubtasks().size(), "Неверное количество подзадач в списке эпика.");
        assertEquals(newSubtask1, newEpic.getSubtasks().get(0), "Подзадачи не совпадают.");
        assertEquals(newSubtask2, newEpic.getSubtasks().get(1), "Подзадачи не совпадают.");
        assertEquals(Status.NEW, newEpic.getStatus(), "Статус эпика с новыми подзадачами не NEW.");

    }

    @Test
    void updateStatusOfEpicWhenAllSubtasksAreDone() {
        Epic newEpic = manager.createEpic(new Epic("Epic2", "Epic2 description", 0, Status.NEW));
        Subtask newSubtask1 = manager.createSubtask(new Subtask("Subtask2", "Subtask2 description",
                0, Status.DONE, 30L, LocalDateTime.of(2022, Month.JUNE, 2, 13, 0),
                newEpic.getId()));
        Subtask newSubtask2 = manager.createSubtask(new Subtask("Subtask3", "Subtask3 description",
                0, Status.DONE, 30L, LocalDateTime.of(2022, Month.JUNE, 2, 14, 0),
                newEpic.getId()));

        assertEquals(2, newEpic.getSubtasks().size(), "Неверное количество подзадач в списке эпика.");
        assertEquals(newSubtask1, newEpic.getSubtasks().get(0), "Подзадачи не совпадают.");
        assertEquals(newSubtask2, newEpic.getSubtasks().get(1), "Подзадачи не совпадают.");
        assertEquals(Status.DONE, newEpic.getStatus(), "Статус эпика с выполненными подзадачами не DONE.");
    }

    @Test
    void updateStatusOfEpicWhenAllSubtasksAreNewAndDone() {
        Epic newEpic = manager.createEpic(new Epic("Epic2", "Epic2 description", 0, Status.NEW));
        Subtask newSubtask1 = manager.createSubtask(new Subtask("Subtask2", "Subtask2 description",
                0, Status.NEW, 30L, LocalDateTime.of(2022, Month.JUNE, 2, 13, 0),
                newEpic.getId()));
        Subtask newSubtask2 = manager.createSubtask(new Subtask("Subtask3", "Subtask3 description",
                0, Status.DONE, 30L, LocalDateTime.of(2022, Month.JUNE, 2, 14, 0),
                newEpic.getId()));

        assertEquals(2, newEpic.getSubtasks().size(), "Неверное количество подзадач в списке эпика.");
        assertEquals(newSubtask1, newEpic.getSubtasks().get(0), "Подзадачи не совпадают.");
        assertEquals(newSubtask2, newEpic.getSubtasks().get(1), "Подзадачи не совпадают.");
        assertEquals(Status.IN_PROGRESS, newEpic.getStatus(), "Статус эпика c подзадачами смешанного типа" +
                "не IN_PROGRESS.");
    }

    @Test
    void updateStatusOfEpicWhenAllSubtasksAreInProgress() {
        Epic newEpic = manager.createEpic(new Epic("Epic2", "Epic2 description", 0, Status.DONE));
        Subtask newSubtask1 = manager.createSubtask(new Subtask("Subtask2", "Subtask2 description",
                0, Status.IN_PROGRESS, 30L,
                LocalDateTime.of(2022, Month.JUNE, 2, 13, 0), newEpic.getId()));
        Subtask newSubtask2 = manager.createSubtask(new Subtask("Subtask3", "Subtask3 description",
                0, Status.IN_PROGRESS, 30L,
                LocalDateTime.of(2022, Month.JUNE, 2, 14, 0), newEpic.getId()));

        assertEquals(2, newEpic.getSubtasks().size(), "Неверное количество подзадач в списке эпика.");
        assertEquals(newSubtask1, newEpic.getSubtasks().get(0), "Подзадачи не совпадают.");
        assertEquals(newSubtask2, newEpic.getSubtasks().get(1), "Подзадачи не совпадают.");
        assertEquals(Status.IN_PROGRESS, newEpic.getStatus(), "Статус эпика c подзадачами, находящимися" +
                "в процессе выполнения, не IN_PROGRESS.");
    }
}