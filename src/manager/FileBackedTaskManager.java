package manager;

import exception.ManagerSaveException;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private String file;

    public FileBackedTaskManager(String file) {
        this.file = file;
    }

    public FileBackedTaskManager() {

    }

    public static FileBackedTaskManager loadFromFile(String file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        manager.load();
        return manager;
    }

    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            writer.append("id,type,name,status,description,epic,duration,start time, end time");
            writer.newLine();
            for (Map.Entry<Integer, Task> entry : taskMap.entrySet()) {
                writer.append(CSVCreator.turnTaskToString(entry.getValue()));
                writer.newLine();
            }
            for (Map.Entry<Integer, Epic> entry : epicMap.entrySet()) {
                writer.append(CSVCreator.turnTaskToString(entry.getValue()));
                writer.newLine();
            }
            for (Map.Entry<Integer, Subtask> entry : subtaskMap.entrySet()) {
                writer.append(CSVCreator.turnTaskToString(entry.getValue()));
                writer.newLine();
            }
            writer.newLine();
            writer.append(CSVCreator.turnHistoryToString(taskHistory));
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }

    public void load() {
        try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            reader.readLine();
            int maxId = 0;
            while (true) {
                String line = reader.readLine();
                if (line.isEmpty()) {
                    break;
                }
                String[] attribute = line.split(",");
                Task task = CSVCreator.makeTaskFromString(line);
                if (task.getId() > maxId) {
                        maxId = task.getId();
                        autoId = maxId;
                }
                switch (attribute[1]) {
                    case "TASK":
                        taskMap.put(task.getId(), task);
                        break;
                    case "EPIC":
                        epicMap.put(task.getId(), (Epic) task);
                        break;
                    case "SUBTASK":
                        subtaskMap.put(task.getId(), (Subtask) task);
                        epicMap.get(((Subtask) task).getEpicId()).getSubtasks().add((Subtask) task);
                        break;
                }
            }
            String line = reader.readLine();
            List<Integer> history = CSVCreator.makeHistoryFromString(line);
            for (Integer id : history) {
                if (taskMap.containsKey(id)) {
                    getTaskById(id);
                } else if (epicMap.containsKey(id)) {
                    getEpicById(id);
                } else {
                    getSubtaskById(id);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = super.getSubtaskById(id);
        save();
        return subtask;
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void removeEpicById(int id) {
        super.removeEpicById(id);
        save();
    }

    @Override
    public void removeSubtaskById(int id) {
        super.removeSubtaskById(id);
        save();
    }

    @Override
    public Task createTask(Task task) {
        Task newTask = super.createTask(task);
        save();
        return newTask;
    }

    @Override
    public Epic createEpic(Epic epic) {
        Epic newEpic = super.createEpic(epic);
        save();
        return newEpic;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        Subtask newSubtask = super.createSubtask(subtask);
        save();
        return newSubtask;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }
}