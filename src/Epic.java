import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Subtask> subtasksList;

    public Epic(String name, String description, int id, String status) {
        super(name, description, id, status);
    }

    public ArrayList<Subtask> getSubtasksList() {
        return subtasksList;
    }

    public void setSubtasksList(ArrayList<Subtask> subtasksList) {
        this.subtasksList = subtasksList;
    }
}