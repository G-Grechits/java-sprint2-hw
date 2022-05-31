package manager;

import exception.TaskValidationException;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected HashMap<Integer, Task> taskMap = new HashMap<>();
    protected HashMap<Integer, Epic> epicMap = new HashMap<>();
    protected HashMap<Integer, Subtask> subtaskMap = new HashMap<>();
    protected Map<LocalDateTime, Task> prioritizedTasks = new TreeMap<>();
    protected HistoryManager taskHistory = Managers.getDefaultHistory();
    protected int autoId = 0;

    private void addToPrioritized(Task task) {
        LocalDateTime startTime = task.getStartTime();
        LocalDateTime endTime = task.getEndTime();
        for (Map.Entry<LocalDateTime, Task> entry : prioritizedTasks.entrySet()) {
            Task existingTask = entry.getValue();
            LocalDateTime existingStart = existingTask.getStartTime();
            LocalDateTime existingEnd = existingTask.getEndTime();
            if (!endTime.isAfter(existingStart)) {
                continue;
            }
            if (!existingEnd.isAfter(startTime)) {
                continue;
            }
            throw new TaskValidationException("Задача пересекается с id=" + existingTask.getId() + " c " + existingStart
                    + " по " + existingEnd);
        }
        prioritizedTasks.put(startTime, task);
    }

    private void removeFromPrioritized(Task task) {
        prioritizedTasks.remove(task.getStartTime());
    }

    private void updateTimeOfEpic(Epic epic) {
        List<Subtask> subtasks = epic.getSubtasks();
        if (subtasks.isEmpty()) {
            epic.setDuration(0);
            return;
        }
        LocalDateTime start = LocalDateTime.MAX;
        LocalDateTime end = LocalDateTime.MIN;
        for (Subtask subtask : subtasks) {
            LocalDateTime startTime = subtask.getStartTime();
            LocalDateTime endTime = subtask.getEndTime();
            if (startTime.isBefore(start)) {
                start = startTime;
            }
            if (endTime.isAfter(end)) {
                end = endTime;
            }
        }
        Duration duration = Duration.between(start, end);
        epic.setDuration(duration.toMinutes());
        epic.setStartTime(start);
        epic.setEndTime(end);
    }

    private void updateStatusOfEpic(Epic epic) {
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

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks.values());
    }

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
        for (Integer id : taskMap.keySet()) {
            taskHistory.remove(id); //из истории удалены все запросы по задачам
        }
        for (Task task : taskMap.values()) {
            removeFromPrioritized(task);
        }
        taskMap.clear(); //удалены все задачи
    }

    @Override
    public void removeAllEpics() {
        for (Integer id : subtaskMap.keySet()) {
            taskHistory.remove(id); //из истории удалены все запросы по подзадачам
        }
        for (Subtask subtask : subtaskMap.values()) {
            removeFromPrioritized(subtask);
        }
        subtaskMap.clear(); //удалены все подзадачи
        for (Integer id : epicMap.keySet()) {
            taskHistory.remove(id); //из истории удалены все запросы по эпикам
        }
        epicMap.clear(); //удалены все эпики
    }

    @Override
    public void removeAllSubtasks() {
         for (Epic epic : epicMap.values()) {
             epic.getSubtasks().clear(); //очищены списки подзадач всех эпиков
             updateStatusOfEpic(epic); //обновлены статусы всех эпиков
             updateTimeOfEpic(epic); //обновлено время всех эпиков
         }
         for (Integer id : subtaskMap.keySet()) {
             taskHistory.remove(id); //из истории удалены все запросы по подзадачам
         }
         for (Subtask subtask : subtaskMap.values()) {
             removeFromPrioritized(subtask);
         }
         subtaskMap.clear(); //удалены все подзадачи
    }

    @Override
    public Task getTaskById(int id) {
        if (taskMap.containsKey(id)) {
            taskHistory.add(taskMap.get(id)); //задача добавлена в историю
            return taskMap.get(id);
        }
        return null;
    }

    @Override
    public Epic getEpicById(int id) {
        if (epicMap.containsKey(id)) {
            taskHistory.add(epicMap.get(id)); //эпик добавлен в историю
            return epicMap.get(id);
        }
        return null;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        if (subtaskMap.containsKey(id)) {
            taskHistory.add(subtaskMap.get(id)); //подзадача добавлена в историю
            return subtaskMap.get(id);
        }
        return null;
    }

    @Override
    public void removeTaskById(int id) {
        if (!taskMap.containsKey(id)) {
            return;
        }
        taskHistory.remove(id); //задача удалена из истории
        removeFromPrioritized(taskMap.get(id));
        taskMap.remove(id);
    }

    @Override
    public void removeEpicById(int id) {
        if (!epicMap.containsKey(id)) {
            return;
        }
        for (Subtask subtask : epicMap.get(id).getSubtasks()) {
            taskHistory.remove(subtask.getId()); //из истории удалены все запросы по подзадачам соответствующего эпика
            removeFromPrioritized(subtask);
            subtaskMap.remove(subtask.getId()); //удалены все подзадачи соответствующего эпика
        }
        taskHistory.remove(id); //эпик удалён из истории
        epicMap.remove(id);
    }

    @Override
    public void removeSubtaskById(int id) {
        if (!subtaskMap.containsKey(id)) {
            return;
        } //подзадача удалена из списка подзадач соответствующего эпика
        epicMap.get(subtaskMap.get(id).getEpicId()).getSubtasks().remove(subtaskMap.get(id));
        updateStatusOfEpic(epicMap.get(subtaskMap.get(id).getEpicId())); //обновлён статус соответствующего эпика
        updateTimeOfEpic(epicMap.get(subtaskMap.get(id).getEpicId())); //обновлено время соответствующего эпика
        taskHistory.remove(id); //подзадача удалена из истории
        removeFromPrioritized(subtaskMap.get(id));
        subtaskMap.remove(id);
    }

    @Override
    public Task createTask(Task task) {
        task.setId(++autoId);
        taskMap.put(task.getId(), task);
        addToPrioritized(task);
        return task;
    }

    @Override
    public Epic createEpic(Epic epic) {
        epic.setId(++autoId);
        ArrayList<Subtask> subtasks = new ArrayList<>();
        epic.setSubtasks(subtasks);
        updateStatusOfEpic(epic);
        updateTimeOfEpic(epic);
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
            updateTimeOfEpic(epicMap.get(subtask.getEpicId()));
        }
        addToPrioritized(subtask);
        return subtask;
    }

    @Override
    public void updateTask(Task task) {
        if (!taskMap.containsKey(task.getId())) {
            return;
        }
        removeFromPrioritized(taskMap.get(task.getId()));
        taskMap.put(task.getId(), task);
        addToPrioritized(task);
    }

    @Override
    public void updateEpic(Epic epic) {
        if (!epicMap.containsKey(epic.getId())) {
            return;
        }
        updateStatusOfEpic(epic);
        updateTimeOfEpic(epic);
        epicMap.put(epic.getId(), epic);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (!subtaskMap.containsKey(subtask.getId())) {
            return;
        }
        if (epicMap.containsKey(subtask.getEpicId())) {
            epicMap.get(subtask.getEpicId()).getSubtasks().remove(subtaskMap.get(subtask.getId()));
            epicMap.get(subtask.getEpicId()).getSubtasks().add(subtask);
            updateStatusOfEpic(epicMap.get(subtask.getEpicId()));
            updateTimeOfEpic(epicMap.get(subtask.getEpicId()));
        }
        removeFromPrioritized(subtaskMap.get(subtask.getId()));
        subtaskMap.put(subtask.getId(), subtask);
        addToPrioritized(subtask);
    }

    @Override
    public ArrayList<Subtask> getSubtasksByEpic(int epicId) {
        if (epicMap.containsKey(epicId)) {
            return epicMap.get(epicId).getSubtasks();
        }
        return null;
    }
}