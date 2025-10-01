import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private static class Node {
        Task task;
        Node prev;
        Node next;

        Node(Task task) {
            this.task = task;
        }
    }

    private final Map<Integer, Node> nodes = new HashMap<>();
    private Node head;
    private Node tail;

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }

        int id = task.getId();
        Node existing = nodes.get(id);

        if (existing != null) {
            removeNode(existing);
        }

        Node newNode = linkLast(task);
        nodes.put(id, newNode);
    }

    @Override
    public void remove(int id) {
        Node node = nodes.remove(id);

        if (node != null) {
            removeNode(node);
        }
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    private Node linkLast(Task task) {
        Node node = new Node(task);

        if (tail == null) {
            head = tail = node;
        } else {
            tail.next = node;
            node.prev = tail;
            tail = node;
        }

        return node;
    }

    private void removeNode(Node node) {
        if (node == null) {
            return;
        }

        Node prev = node.prev;
        Node next = node.next;

        if (prev != null) {
            prev.next = next;
        } else {
            head = next;
        }

        if (next != null) {
            next.prev = prev;
        } else {
            tail = prev;
        }

        node.prev = null;
        node.next = null;
        node.task = null;
    }

    private List<Task> getTasks() {
        List<Task> result = new ArrayList<>();
        Node current = head;

        while (current != null) {
            result.add(current.task);
            current = current.next;
        }

        return result;
    }
}
