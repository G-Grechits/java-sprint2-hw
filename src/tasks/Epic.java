package tasks;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Subtask> subtasks;

    public Epic(String name, String description, int id, Status status) {
        super(name, description, id, status);
        subtasks = new ArrayList<>();
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(ArrayList<Subtask> subtasks) {
        this.subtasks = subtasks;
    }

    @Override
    public Type getType() {
        return Type.EPIC;
    }

    @Override
    public String toString() {
        return "Эпик{" +
                "название: '" + name + '\'' +
                ", описание: '" + description + '\'' +
                ", ID = " + id +
                ", статус: '" + status + '\'' +
                ", подзадачи: " + subtasks +
                '}';
    }
}