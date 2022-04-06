package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.List;

public interface TaskManager {
    List<Task> getHistory(); //метод получения истории просмотров

    List<Task> getAllTasks(); //метод получения списка всех задач

    List<Epic> getAllEpics(); //метод получения списка всех эпиков

    List<Subtask> getAllSubtasks(); //метод получения списка всех подзадач

    void removeAllTasks(); //метод удаления всех задач

    void removeAllEpics(); //метод удаления всех эпиков

    void removeAllSubtasks(); //метод удаления всех подзадач

    Task getTaskById(int id); //метод получения задачи по идентификатору

    Epic getEpicById(int id); //метод получения эпика по идентификатору

    Subtask getSubtaskById(int id); //метод получения подзадачи по идентификатору

    void removeTaskById(int id); //метод удаления задачи по идентификатору

    void removeEpicById(int id); //метод удаления эпика по идентификатору

    void removeSubtaskById(int id); //метод удаления подзадачи по идентификатору

    Task createTask(Task task); //метод создания задачи

    Epic createEpic(Epic epic); //метод создания эпика

    Subtask createSubtask(Subtask subtask); //метод создания подзадачи

    void updateTask(Task task); //метод обновления задачи

    void updateEpic(Epic epic); //метод обновления эпика

    void updateSubtask(Subtask subtask); //метод обновления подзадачи

    List<Subtask> getSubtasksByEpic(int epicId); //метод получения списка всех подзадач определённого эпика

    void updateStatusOfEpic(Epic epic); //метод обновления статуса эпика
}