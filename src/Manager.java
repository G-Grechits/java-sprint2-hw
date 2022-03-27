import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private HashMap<Integer, Task> taskMap = new HashMap<>();
    private HashMap<Integer, Epic> epicMap = new HashMap<>();
    private HashMap<Integer, Subtask> subtaskMap = new HashMap<>();
    private int autoId = 0;

    public ArrayList<Task> getAllTasks() { //метод получения списка всех задач
        return new ArrayList<>(taskMap.values());
    }

    public ArrayList<Epic> getAllEpics() { //метод получения списка всех эпиков
        return new ArrayList<>(epicMap.values());
    }

    public ArrayList<Subtask> getAllSubtasks() { //метод получения списка всех подзадач
        return new ArrayList<>(subtaskMap.values());
    }

    public void removeAllTasks() { //метод удаления всех задач
        taskMap.clear();
    }

    public void removeAllEpics() { //метод удаления всех эпиков
        epicMap.clear();
    }

    public void removeAllSubtasks() { //метод удаления всех подзадач
        subtaskMap.clear();
    }

    public Task getTaskById(int id) { //метод получения задачи по идентификатору
        return taskMap.get(id);
    }

    public Epic getEpicById(int id) { //метод получения эпика по идентификатору
        return epicMap.get(id);
    }

    public Subtask getSubtaskById(int id) { //метод получения подзадачи по идентификатору
        return subtaskMap.get(id);
    }

    public void removeTaskById(int id) { //метод удаления задачи по идентификатору
        taskMap.remove(id);
    }

    public void removeEpicById(int id) { //метод удаления эпика по идентификатору
        epicMap.remove(id);
    }

    public void removeSubtaskById(int id) { //метод удаления подзадачи по идентификатору
        subtaskMap.remove(id);
    }

    public Task createTask(Task task) { //метод создания задачи
        task.setId(++autoId);
        taskMap.put(task.getId(), task);
        return task;
    }

    public Epic createEpic(Epic epic) { //метод создания эпика
        epic.setId(++autoId);
        ArrayList<Subtask> subtasks = new ArrayList<>();
        epic.setsubtasks(subtasks);
        updateStatusOfEpic(epic);
        epicMap.put(epic.getId(), epic);
        return epic;
    }

    public Subtask createSubtask(Subtask subtask) { //метод создания подзадачи
        subtask.setId(++autoId);
        subtaskMap.put(subtask.getId(), subtask);
        if (epicMap.containsKey(subtask.getEpicId())) {
            epicMap.get(subtask.getEpicId()).getsubtasks().add(subtaskMap.get(subtask.getId()));
            updateStatusOfEpic(epicMap.get(subtask.getEpicId()));
        }
        return subtask;
    }

    public void updateTask(Task task) { //метод обновления задачи
        if (!taskMap.containsKey(task.getId())) {
            return;
        }
        taskMap.put(task.getId(), task);
    }

    public void updateEpic(Epic epic) { //метод обновления эпика
        if (!epicMap.containsKey(epic.getId())) {
            return;
        }
        updateStatusOfEpic(epic);
        epicMap.put(epic.getId(), epic);
    }

    public void updateSubtask(Subtask subtask) { //метод обновления подзадачи
        if (!subtaskMap.containsKey(subtask.getId())) {
            return;
        }
        subtaskMap.put(subtask.getId(), subtask);
        if (epicMap.containsKey(subtask.getEpicId())) {
            epicMap.get(subtask.getEpicId()).getsubtasks().add(subtaskMap.get(subtask.getId()));
            updateStatusOfEpic(epicMap.get(subtask.getEpicId()));
        }
    }

    public ArrayList<Subtask> getSubtasksByEpic(int epicId) { //метод получения списка всех подзадач определённого эпика
        if (epicMap.containsKey(epicId)) {
            return epicMap.get(epicId).getsubtasks();
        }
        return null;
    }

    private void updateStatusOfEpic(Epic epic) { //метод для обновления статуса эпика
        if (epic.getsubtasks().isEmpty()) {
            epic.setStatus(Status.newStatus);
        } else {
            for (Subtask subtask : epic.getsubtasks()) {
                if (subtask.getStatus().equals(Status.newStatus)) {
                    epic.setStatus(Status.newStatus);
                } else if (subtask.getStatus().equals(Status.done)) {
                    epic.setStatus(Status.done);
                } else {
                    epic.setStatus(Status.inProgress);
                }
            }
        }
    }
}