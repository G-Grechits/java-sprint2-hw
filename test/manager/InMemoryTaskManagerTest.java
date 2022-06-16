package manager;

import exception.TaskValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @BeforeEach
    @Override
    void initializeAttributes() {
        manager = new InMemoryTaskManager();
        super.initializeAttributes();
    }

    @Test
    void shouldNotAddTasksInTreeMap() {
        manager = new InMemoryTaskManager();
        Task newTask = manager.createTask(new Task("Task №1", "Test task №1", 1, Status.IN_PROGRESS, 120,
                LocalDateTime.of(2022, 04, 11, 9, 00)));
        assertEquals(1, manager.getPrioritizedTasks().size());
        assertThrows(
                TaskValidationException.class,
                () -> manager.createTask(newTask)
        );
        assertEquals(1, manager.getPrioritizedTasks().size());
        Epic newEpic = new Epic("Epic №1", "Test epic №1", 2, Status.NEW);
        manager.createEpic(newEpic);
        Subtask newSubtask = manager.createSubtask(new Subtask("Subtask №1", "Test subtask №1", 3, Status.IN_PROGRESS,
                90, LocalDateTime.of(2021, 04, 11, 12, 00), newEpic.getId()));
        assertEquals(2, manager.getPrioritizedTasks().size());
        assertThrows(
                TaskValidationException.class,
                () -> manager.createTask(newSubtask)
        );
        assertEquals(2, manager.getPrioritizedTasks().size());
    }
}