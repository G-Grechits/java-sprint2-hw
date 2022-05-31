package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;

import java.io.*;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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
            assertNull(reader.readLine(), "Строка в файле не равна null.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void saveEpicWithoutSubtasks() {
        clearFile();
        manager = new FileBackedTaskManager(filePath);
        Epic newEpic = new Epic("Epic1", "Epic1 description", 0, Status.NEW);
        LocalDateTime dateTime = LocalDateTime.of(2022, Month.JUNE,1,18,30);
        newEpic.setStartTime(dateTime);
        newEpic.setEndTime(dateTime);
        manager.createEpic(newEpic);

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            reader.readLine();
            assertEquals("1,EPIC,Epic1,NEW,Epic1 description, ,0,01.06.2022 18:30,01.06.2022 18:30",
                    reader.readLine(), "Выводимые строки не совпадают.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}