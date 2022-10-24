public class CacheList<K, V> {
    // First node of DoublyLinkedList
    DoublyLinkedListNode<K, V> start;
    // Last node of DoublyLinkedList
    DoublyLinkedListNode<K, V> end;

    public CacheList(DoublyLinkedListNode<K, V> start, DoublyLinkedListNode<K, V> end) {
        this.start = start;
        this.end = end;
    }


    // Remove last node from queue
    public void removeLastNode() {
        System.out.println("Capacity exceeded so removing oldest cache object " + end.value
                + " for adding the new object to the cache");
        end = end.prev;
        if (end != null) {
            end.next = null;
        }
    }

    // Add node in front of queue
    public void addItemToFront(DoublyLinkedListNode<K, V> node) {
        node.next = start;
        node.prev = null;
        if (start != null) {
            start.prev = node;
        }
        start = node;
        if (end == null) {
            end = node;
        }
    }
    public void removeNode(DoublyLinkedListNode<K, V> node) {
        DoublyLinkedListNode<K, V> prevNode = node.prev;
        DoublyLinkedListNode<K, V> nextNode = node.next;

        if (prevNode != null) {
            prevNode.next = nextNode;
        } else {
            start = nextNode;
        }

        if (nextNode != null) {
            nextNode.prev = prevNode;
        } else {
            end = prevNode;
        }
    }

    // Reorder existing node to front of queue
    public void bringItemToFront(DoublyLinkedListNode<K, V> node) {
        removeNode(node);
        addItemToFront(node);
    }

}
