package manager;

import tasks.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task task); //метод добавления задачи в историю

    void remove(int id); //метод удаления задачи из истории

    List<Task> getHistory(); //метод получения истории задач
}