package manager;

import org.junit.jupiter.api.BeforeEach;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @BeforeEach
    @Override
    void initializeAttributes() {
        manager = new InMemoryTaskManager();
        super.initializeAttributes();
    }
}