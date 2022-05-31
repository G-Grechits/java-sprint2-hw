package tasks;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Subtask> subtasks;
    private LocalDateTime endTime;

    public Epic(String name, String description, int id, Status status) {
        super(name, description, id, status, 0L, LocalDateTime.now());
        endTime = startTime;
        subtasks = new ArrayList<>();
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(ArrayList<Subtask> subtasks) {
        this.subtasks = subtasks;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
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