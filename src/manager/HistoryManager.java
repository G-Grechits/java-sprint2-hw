package manager;

import tasks.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task task); //метод добавления задачи в историю

    List<Task> getHistory(); //метод получения истории задач
}