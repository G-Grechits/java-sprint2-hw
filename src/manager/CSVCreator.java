package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.Status;
import tasks.Type;

import java.util.List;
import java.util.ArrayList;

public class CSVCreator {

    public static String turnTaskToString(Task task) {
        StringBuilder taskInLine = new StringBuilder();
        taskInLine.append(task.getId() + "," + task.getType() + "," + task.getName() + "," + task.getStatus() + ","
                + task.getDescription());
        if (task.getType().equals(Type.SUBTASK)) {
            taskInLine.append("," + ((Subtask) task).getEpicId());
        }
        return taskInLine.toString();
    }

    public static Task makeTaskFromString(String value) {
        String[] attribute = value.split(",");
        if (attribute.length <= 1) {
            return null;
        }
        switch (attribute[1]) {
            case "TASK":
                return new Task(attribute[2], attribute[4], Integer.parseInt(attribute[0]),
                        Status.valueOf(attribute[3]));
            case "EPIC":
                return new Epic(attribute[2], attribute[4], Integer.parseInt(attribute[0]),
                        Status.valueOf(attribute[3]));
            case "SUBTASK":
                return new Subtask(attribute[2], attribute[4], Integer.parseInt(attribute[0]),
                        Status.valueOf(attribute[3]), Integer.parseInt(attribute[5]));
        }
        return null;
    }

    public static String turnHistoryToString(HistoryManager manager) {
        StringBuilder history = new StringBuilder();
        for (Task task : manager.getHistory()) {
            history.append(task.getId() + ",");
        }
        if (history.length() != 0) {
            history.deleteCharAt(history.length() - 1);
        }
        return history.toString();
    }

    public static List<Integer> makeHistoryFromString(String value) {
        List<Integer> history = new ArrayList<>();
        String[] id = value.split(",");
        for (String number : id) {
            history.add(Integer.parseInt(number));
        }
        return history;
    }
}