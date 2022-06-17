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

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public Type getType() {
        return Type.SUBTASK;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "epicId=" + epicId +
                ", name:'" + name + '\'' +
                ", description:'" + description + '\'' +
                ", id=" + id +
                ", status:" + status +
                ", duration:" + duration +
                ", startTime:" + startTime +
                ", endTime:" + getEndTime() +
                '}';
    }
}