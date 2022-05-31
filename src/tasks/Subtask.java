package tasks;

import java.time.LocalDateTime;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String name, String description, int id, Status status, long duration, LocalDateTime startTime, int epicId) {
        super(name, description, id, status, duration, startTime);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public Type getType() {
        return Type.SUBTASK;
    }

    @Override
    public String toString() {
        return "Подзадача{" +
                "название: '" + name + '\'' +
                ", описание: '" + description + '\'' +
                ", ID = " + id +
                ", статус: '" + status + '\'' +
                ", ID эпика = " + epicId +
                '}';
    }
}