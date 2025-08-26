import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {

    @Test
    void constructorShouldSetAllFields() {
        Subtask subtask = new Subtask(1, "Subtask", "Описание", Status.NEW, 100);

        assertEquals(1, subtask.getId());
        assertEquals("Subtask", subtask.getName());
        assertEquals("Описание", subtask.getDescription());
        assertEquals(Status.NEW, subtask.getStatus());
        assertEquals(100, subtask.getEpicId());
    }

    @Test
    void getEpicId_shouldReturnCorrectEpicId() {
        Subtask subtask = new Subtask(2, "Subtask2", "Описание2", Status.IN_PROGRESS, 200);
        assertEquals(200, subtask.getEpicId(), "Subtask должен хранить правильный epicId");
    }

    @Test
    void subtaskEquals_shouldReturnTrueForSameId() {
        Subtask s1 = new Subtask(1, "A", "Desc", Status.NEW, 100);
        Subtask s2 = new Subtask(1, "B", "Desc2", Status.DONE, 100);

        assertEquals(s1, s2, "Subtask с одинаковым id должны считаться равными");
        assertEquals(s1.hashCode(), s2.hashCode(), "HashCode должен совпадать для одинакового id");
    }

    @Test
    void subtaskEquals_shouldReturnFalseForDifferentId() {
        Subtask s1 = new Subtask(1, "A", "Desc", Status.NEW, 100);
        Subtask s2 = new Subtask(2, "B", "Desc", Status.NEW, 100);

        assertNotEquals(s1, s2, "Subtask с разными id не должны быть равны");
    }
}
