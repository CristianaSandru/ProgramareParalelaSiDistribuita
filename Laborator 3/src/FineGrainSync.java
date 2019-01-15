public class FineGrainSync extends SortedLinkedList {
    @Override
    public void insert(float value) {
        mutex.lock();
        if(first == null) {
            first = new Node(value);
            mutex.unlock();
        }
        else if (first.getValue() > value) {
            Node nodeToAdd = new Node(value);
            nodeToAdd.setNext(first);
            first = nodeToAdd;
            mutex.unlock();
        }
        else if(first.getNext() == null){
            Node nodeToAdd = new Node(value);
            first.setNext(nodeToAdd);
            mutex.unlock();
        }
        else {
            mutex.unlock();
            Node nodeToAddAfter = first;
            Node nodeToAddBefore = first.getNext();

            nodeToAddAfter.getMutex().lock();
            nodeToAddBefore.getMutex().lock();

            while ( true ) {
                if (nodeToAddBefore.getValue() > value) {
                    Node nodeToAdd = new Node(value);

                    nodeToAdd.setNext(nodeToAddBefore);
                    nodeToAddAfter.setNext(nodeToAdd);

                    nodeToAddAfter.getMutex().unlock();
                    nodeToAddBefore.getMutex().unlock();
                    break;
                }

                Node nodeToAddAfterSup = nodeToAddAfter;
                nodeToAddAfter = nodeToAddBefore;

                nodeToAddAfterSup.getMutex().unlock();

                nodeToAddBefore = nodeToAddBefore.getNext();
                if (nodeToAddBefore != null) {
                    nodeToAddBefore.getMutex().lock();
                }
                else {
                    nodeToAddAfter.setNext(new Node(value));
                    nodeToAddAfter.getMutex().unlock();
                    break;
                }
            }
        }
    }

    @Override
    public void remove(float value) {
        mutex.lock();

        if (first == null || first.getNext() == null) {
            mutex.unlock();
            return;
        }

        if(this.first.getValue()==value) {
            this.first = this.first.getNext();
            mutex.unlock();
            return;
        }

        mutex.unlock();

        Node nodeToDeleteBefore = first;
        Node nodeToDelete = first.getNext();

        nodeToDelete.getMutex().lock();
        nodeToDeleteBefore.getMutex().lock();

        while ( true ) {
            if (nodeToDelete.getValue() > value) {
                nodeToDelete.getMutex().unlock();
                nodeToDeleteBefore.getMutex().unlock();
                break;
            }

            if (nodeToDelete.getValue() == value) {
                nodeToDeleteBefore.setNext(nodeToDelete.getNext());
                nodeToDeleteBefore.getMutex().unlock();
                nodeToDelete.getMutex().unlock();
                break;
            }

            Node nodeToDeleteBeforeSup = nodeToDeleteBefore;
            nodeToDeleteBefore = nodeToDelete;
            nodeToDeleteBeforeSup.getMutex().unlock();


            if (nodeToDelete.getNext() != null) {
                nodeToDelete.getNext().getMutex().lock();
                nodeToDelete = nodeToDelete.getNext();
            }
            else {
                nodeToDeleteBefore.getMutex().unlock();
                break;
            }
        }
    }
}
