package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    public static final int MAX_HISTORY_SIZE = 10; //добавлена константа ограничения по количеству хранимых задач
    private List<Task> history = new ArrayList<>(); //LinkedList мы ещё не проходили, поэтому пока не использую

    @Override
    public void add(Task task) {
        history.add(task);
        if (history.size() > MAX_HISTORY_SIZE) {
            history.remove(0);
        }
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }
}