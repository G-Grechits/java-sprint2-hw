package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    String filePath = "test/resources/file1.csv";

    private void clearFile() {
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.append("");
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    @BeforeEach
    @Override
    void initializeAttributes() {
        manager = new FileBackedTaskManager(filePath);
        super.initializeAttributes();
    }

    @Test
    void saveEmptyTaskList() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            writer.write("");
            writer.close();
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            reader.readLine();
            assertNull(reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void saveEpicWithoutSubtasks() {
        clearFile();
        manager = new FileBackedTaskManager(filePath);
        manager.createEpic(new Epic("Epic1", "Epic1 description", 0, Status.NEW));

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            reader.readLine();
            assertEquals("1,EPIC,Epic1,NEW,Epic1 description, ,0,31.05.2022 03:54,31.05.2022 03:54",
                    reader.readLine());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}