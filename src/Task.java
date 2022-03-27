public class Task {
    private String name;
    private String description;
    private int id;
    private String status;

    public Task(String name, String description, int id, String status) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Задача{" +
                "название: '" + name + '\'' +
                ", описание: '" + description + '\'' +
                ", ID = " + id +
                ", статус: '" + status + '\'' +
                '}';
    }
}