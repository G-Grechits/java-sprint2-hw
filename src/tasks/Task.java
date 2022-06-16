package tasks;

import java.time.LocalDateTime;

public class Task {
    protected String name;
    protected String description;
    protected int id;
    protected Status status;
    protected long duration;
    protected LocalDateTime startTime;

    public Task(String name, String description, int id, Status status, long duration, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public long getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return startTime.plusMinutes(duration);
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Type getType() {
        return Type.TASK;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name:'" + name + '\'' +
                ", description:'" + description + '\'' +
                ", id=" + id +
                ", status:" + status +
                ", duration:" + duration +
                ", startTime:" + startTime +
                ", endTime:" + getEndTime() +
                '}';
    }
}