package manager;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private HashMap<Integer, Task> taskMap = new HashMap<>();
    private HashMap<Integer, Epic> epicMap = new HashMap<>();
    private HashMap<Integer, Subtask> subtaskMap = new HashMap<>();
    private HistoryManager taskHistory = Managers.getDefaultHistory();
    private int autoId = 0;

    @Override
    public List<Task> getHistory() {
        return taskHistory.getHistory();
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(taskMap.values());
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epicMap.values());
    }

    @Override
    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtaskMap.values());
    }

    @Override
    public void removeAllTasks() {
        taskMap.clear();
    }

    @Override
    public void removeAllEpics() {
        subtaskMap.clear(); //добавлена логика: если нет эпиков - значит, нет и подзадач
        epicMap.clear();
    }

    @Override
    public void removeAllSubtasks() {
         for (Epic epic : epicMap.values()) {
             epic.getSubtasks().clear(); //очищены списки подзадач всех эпиков
             updateStatusOfEpic(epic); //обновлены статусы всех эпиков
         }
         subtaskMap.clear();
    }

    @Override
    public Task getTaskById(int id) {
        taskHistory.add(taskMap.get(id));
        return taskMap.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        taskHistory.add(epicMap.get(id));
        return epicMap.get(id);
    }

    @Override
    public Subtask getSubtaskById(int id) {
        taskHistory.add(subtaskMap.get(id));
        return subtaskMap.get(id);
    }

    @Override
    public void removeTaskById(int id) {
        if (!taskMap.containsKey(id)) {
            return;
        }
        taskMap.remove(id);
    }

    @Override
    public void removeEpicById(int id) {
        if (!epicMap.containsKey(id)) {
            return;
        }
        for (Subtask subtask : epicMap.get(id).getSubtasks()) {
            subtaskMap.remove(subtask.getId()); //добавлена логика: если нет эпика - значит, нет и его подзадач
        }
        epicMap.remove(id);
    }

    @Override
    public void removeSubtaskById(int id) {
        //подзадача удалена из списка подзадач соответствующего эпика
        epicMap.get(subtaskMap.get(id).getEpicId()).getSubtasks().remove(subtaskMap.get(id));
        updateStatusOfEpic(epicMap.get(subtaskMap.get(id).getEpicId())); //обновлён статус соответствующего эпика
        subtaskMap.remove(id);
    }

    @Override
    public Task createTask(Task task) {
        task.setId(++autoId);
        taskMap.put(task.getId(), task);
        return task;
    }

    @Override
    public Epic createEpic(Epic epic) {
        epic.setId(++autoId);
        ArrayList<Subtask> subtasks = new ArrayList<>();
        epic.setSubtasks(subtasks);
        updateStatusOfEpic(epic);
        epicMap.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        subtask.setId(++autoId);
        subtaskMap.put(subtask.getId(), subtask);
        if (epicMap.containsKey(subtask.getEpicId())) {
            epicMap.get(subtask.getEpicId()).getSubtasks().add(subtask);
            updateStatusOfEpic(epicMap.get(subtask.getEpicId()));
        }
        return subtask;
    }

    @Override
    public void updateTask(Task task) {
        if (!taskMap.containsKey(task.getId())) {
            return;
        }
        taskMap.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {
        if (!epicMap.containsKey(epic.getId())) {
            return;
        }
        updateStatusOfEpic(epic);
        epicMap.put(epic.getId(), epic);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (!subtaskMap.containsKey(subtask.getId())) {
            return;
        }
        if (epicMap.containsKey(subtask.getEpicId())) {
            epicMap.get(subtask.getEpicId()).getSubtasks().remove(subtaskMap.get(subtask.getId()));
            subtaskMap.put(subtask.getId(), subtask);
            epicMap.get(subtask.getEpicId()).getSubtasks().add(subtask);
            updateStatusOfEpic(epicMap.get(subtask.getEpicId()));
        }
    }

    @Override
    public ArrayList<Subtask> getSubtasksByEpic(int epicId) {
        if (epicMap.containsKey(epicId)) {
            return epicMap.get(epicId).getSubtasks();
        }
        return null;
    }

    @Override
    public void updateStatusOfEpic(Epic epic) {
        int counterNew = 0;
        int counterDone = 0;

        if (epic.getSubtasks().isEmpty()) {
            epic.setStatus(Status.NEW);
        } else {
            for (Subtask subtask : epic.getSubtasks()) {
                if (subtask.getStatus() == Status.NEW) {
                    counterNew++;
                } if (subtask.getStatus() == Status.DONE) {
                    counterDone++;
                }
            }
            if (counterNew == epic.getSubtasks().size()) {
                epic.setStatus(Status.NEW);
            } else if (counterDone == epic.getSubtasks().size()) {
                epic.setStatus(Status.DONE);
            } else {
                epic.setStatus(Status.IN_PROGRESS);
            }
        }
    }
}