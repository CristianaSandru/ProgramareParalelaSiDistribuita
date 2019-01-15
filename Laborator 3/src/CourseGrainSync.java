public class CourseGrainSync extends SortedLinkedList {
        public void insert(float value) {
            mutex.lock();
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

                while ( true ) {
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
            mutex.unlock();
        }

        public void remove(float value) {
            mutex.lock();
            if (first.getValue() == value) {
                first = first.getNext();
                mutex.unlock();
                return;
            }

            if (first == null || first.getNext() == null) {
                mutex.unlock();
                return;
            }
            Node nodeToDeleteBefore = first;
            Node nodeToDelete = first.getNext();

            while ( true ) {
                if (nodeToDelete.getValue() > value) {
                    mutex.unlock();
                    return;
                }

                if (nodeToDelete.getValue() == value) {
                    nodeToDeleteBefore.setNext(nodeToDelete.getNext());
                    mutex.unlock();
                    return;
                }

                nodeToDeleteBefore = nodeToDelete;
                nodeToDelete = nodeToDelete.getNext();
            }
        }
    }
