import managers.FileBackedTaskManager;
import org.junit.jupiter.api.Test;
import tasks.Task;
import tasks.Status;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    @Override
    protected FileBackedTaskManager createTaskManager() {
        return new FileBackedTaskManager(new File("test_tasks.csv"));
    }

    @Test
    void loadFromFile_shouldRestoreTasks() {
        // Пока оставь пустым
    }
}