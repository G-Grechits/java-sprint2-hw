package manager;

public class Managers {

    public static TaskManager getDefault() {
        return new HttpTaskManager();
    }

    public static TaskManager getManagerWithStorageInFile(String file) {
        return new FileBackedTaskManager(file);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}