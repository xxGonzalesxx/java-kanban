import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    @Test
    void getSubtaskIds_shouldReturnEmptyListWhenNoSubtasksAdded() {
        Epic epic = new Epic(1, "Эпик 1", "Описание эпика", Status.NEW);

        assertTrue(epic.getSubtaskIds().isEmpty(), "У нового эпика список подзадач должен быть пустым");
    }

    @Test
    void addSubtaskId_shouldAddIdsCorrectly() {
        Epic epic = new Epic(2, "Эпик 2", "Описание", Status.NEW);

        epic.addSubtaskId(10);
        epic.addSubtaskId(20);

        List<Integer> ids = epic.getSubtaskIds();

        assertEquals(2, ids.size(), "Должно быть 2 id в списке");
        assertTrue(ids.contains(10), "Список должен содержать id = 10");
        assertTrue(ids.contains(20), "Список должен содержать id = 20");
    }

    @Test
    void clearSubtaskIds_shouldRemoveAllIds() {
        Epic epic = new Epic(3, "Эпик 3", "Описание", Status.NEW);

        epic.addSubtaskId(5);
        epic.addSubtaskId(6);

        assertFalse(epic.getSubtaskIds().isEmpty(), "Перед очисткой список не должен быть пустым");

        epic.clearSubtaskIds();

        assertTrue(epic.getSubtaskIds().isEmpty(), "После очистки список должен быть пустым");
    }
    }