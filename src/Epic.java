import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Subtask> subtasks;

    public Epic(String name, String description, int id, String status) {
        super(name, description, id, status);
    }

    public ArrayList<Subtask> getsubtasks() {
        return subtasks;
    }

    public void setsubtasks(ArrayList<Subtask> subtasks) {
        this.subtasks = subtasks;
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