package manager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.Type;

import java.util.ArrayList;

public class HttpTaskManager extends FileBackedTaskManager {
    private Gson gson = HttpTaskServer.getGson();
    private KVTaskClient client = new KVTaskClient("http://localhost:8078");

    public HttpTaskManager() {
        load();
    }

    @Override
    public void save() {
        if (!getAllTasks().isEmpty()) {
            client.save("task", gson.toJson(getAllTasks()));
        }
        if (!getAllEpics().isEmpty()) {
            client.save("epic", gson.toJson(getAllEpics()));
        }
        if (!getAllSubtasks().isEmpty()) {
            client.save("subtask", gson.toJson(getAllSubtasks()));
        }
        if (!getHistory().isEmpty()) {
            client.save("history", gson.toJson(getHistory()));
        }
    }

    @Override
    public void load() {
        try {
            ArrayList<Task> tasks = gson.fromJson(client.load("task"),
                    new TypeToken<ArrayList<Task>>() {
                    }.getType());
            for (Task task : tasks) {
                createTask(task);
            }
        } catch (NullPointerException e) {
            System.out.println("Список задач пуст.");
        }

        try {
            ArrayList<Epic> epics = gson.fromJson(client.load("epic"),
                    new TypeToken<ArrayList<Epic>>(){
                    }.getType());
            for (Epic epic : epics) {
                createEpic(epic);
            }
        } catch (NullPointerException e) {
            System.out.println("Список эпиков пуст.");
        }

        try {
            ArrayList<Subtask> subtasks = gson.fromJson(client.load("subtask"),
                    new TypeToken<ArrayList<Subtask>>(){
                    }.getType());
            for (Subtask subtask : subtasks) {
                createSubtask(subtask);
            }
        } catch (NullPointerException e) {
            System.out.println("Список подзадач пуст.");
        }

        try {
            ArrayList<Task> history = gson.fromJson(client.load("history"),
                    new TypeToken<ArrayList<Task>>(){
                    }.getType());
            for (Task task : history) {
                if (task.getType() == Type.TASK) {
                    getTaskById(task.getId());
                } else if (task.getType() == Type.EPIC) {
                    getEpicById(task.getId());
                } else getSubtaskById(task.getId());
            }
        } catch (NullPointerException e) {
            System.out.println("История задач пуста.");
        }
    }
}