import java.util.Iterator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SortedLinkedList {
    protected Node first;
    protected Lock mutex;

    class It implements Iterator {
        private Node node;
        public It(Node node) {
            this.node = node;
            mutex.lock();
        }

        @Override
        public boolean hasNext() {
            if (node == null) {
                mutex.unlock();
                return false;
            }
            return true;
        }

        @Override
        public Float next() {
            Float value = node.getValue();
            node = node.getNext();
            return value;
        }
    }

    public Node getFirst() {
        return first;
    }

    public It getIterator() {
        return new It(first);
    }

    public SortedLinkedList() {
        this.first = null;
        this.mutex = new ReentrantLock();
    }

    public void insert(float value) {
        if (first == null) {
            first = new Node(value);
        }
        else if (first.getValue() > value) {
            Node nodeToAdd = new Node(value);
            nodeToAdd.setNext(first);
            first = nodeToAdd;
        }
        else if (first.getNext() == null) {
            Node nodeToAdd =  new Node(value);
            first.setNext(nodeToAdd);
        }
        else {
            Node nodeToAddAfter = first;
            Node nodeToAddBefore = first.getNext();

            while (nodeToAddBefore != null) {
                if(nodeToAddBefore.getValue() > value) {
                    Node nodeToAdd = new Node(value);
                    nodeToAdd.setNext(nodeToAddBefore);
                    nodeToAddAfter.setNext(nodeToAdd);
                    break;
                }
                nodeToAddAfter = nodeToAddBefore;
                if (nodeToAddBefore.getNext() != null) {
                    nodeToAddBefore = nodeToAddBefore.getNext();
                } else {
                    nodeToAddBefore.setNext(new Node(value));
                    break;
                }
            }
        }
    }

    public void remove(float value) {
        if (first.getValue() == value) {
            first = first.getNext();
            return;
        }

        if (first == null || first.getNext() == null)
            return;

        Node nodeToDeleteBefore = first;
        Node nodeToDelete = first.getNext();

        while (nodeToDelete != null) {
            if (nodeToDelete.getValue() > value)
                break;

            if (nodeToDelete.getValue() == value) {
                nodeToDeleteBefore.setNext(nodeToDelete.getNext());
                break;
            }

            nodeToDeleteBefore = nodeToDelete;
            nodeToDelete = nodeToDelete.getNext();
        }
    }
}
