package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private Map<Integer, Node<Task>> nodeMap = new HashMap<>();
    private Node<Task> first;
    private Node<Task> last;

    private static class Node<E> {
        private E item;
        private Node<E> next;
        private Node<E> prev;

        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }

    private void linkLast(Task task) {
        final Node<Task> l = last;
        final Node<Task> newNode = new Node<>(l, task, null);
        last = newNode;
        if (l == null)
            first = newNode;
        else
            l.next = newNode;
        nodeMap.put(task.getId(), newNode);
    }

    private void removeNode(Node<Task> node) {
            if (node == first) {
                first.prev = null;
                first = first.next;
            } else if (node == last) {
                last = last.prev;
                last.next = null;
            } else {
                node.prev.next = node.next;
                node.next.prev = node.prev;
            }
    }

    private List<Task> getTasks() {
        List<Task> history = new ArrayList<>();
        Node<Task> node = first;
        while (node != null) {
            history.add(node.item);
            node = node.next;
        }
        return history;
    }

    @Override
    public void add(Task task) {
        Node<Task> node = nodeMap.get(task.getId());
        if (node != null) {
            removeNode(node);
        }
        linkLast(task);
    }

    @Override
    public void remove(int id) {
        Node<Task> node = nodeMap.remove(id);
        if (node != null) {
            removeNode(node);
        }
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }
}