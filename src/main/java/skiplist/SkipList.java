package skiplist;


import java.util.AbstractList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

/**
 * 8/26/16 9:23 PM
 */
public class SkipList<T extends Comparable<T>> extends AbstractList<T> {

    protected static Random r = new Random();

    /** head terminal for all the linked-lists */
    private SLNode<T> head = new SLNode<>(null);
    /** tail terminal for all the linked-lists */
    private SLNode<T> tail = new SLNode<>(null);

    public SkipList(T[] something) {
        Collections.addAll(this, something);
    }

    public SkipList() {
    }

    @Override public Iterator<T> iterator() {
        return new Iterator<T>() {
            SLNode<T> current = head;

            @Override public boolean hasNext() {
                return current.getNextAtLevel(0) != tail;
            }

            @Override public T next() {
                if (!hasNext()) throw new NoSuchElementException();
                current = current.getNextAtLevel(0);
                return current.getElem();
            }
        };
    }

    @Override public int size() {
        final int[] size = {0};
        iterator().forEachRemaining(t -> size[0]++);
        return size[0];
    }

    /** NB: this is not thread safe or reentrant */
    @Override public boolean add(T elem) {
        /* floor is empty when there are no less-or-equal elements.
         * It is null when the skip-list is empty */
        List<SLNode<T>> prevNodes = floor(elem);
        if (prevNodes == null) {
            addWhenEmpty(elem);
            return true;
        } else if (prevNodes.isEmpty()) {
            addToBeginning(elem);
            return true;
        }
        /* add it after the floor */
        SLNode<T> newNode = new SLNode<>(elem);
        for (int level = 0; level == 0 || r.nextBoolean(); level++) {
            SLNode<T> prevNode = level < prevNodes.size() ? prevNodes.get(level) : head;
            SLNode<T> wasNext = level < prevNodes.size() ? prevNode.getNextAtLevel(level) : tail;
            prevNode.setNextAtLevel(level, newNode);
            newNode.setNextAtLevel(level, wasNext);
        }
        return true;
    }

    private void addToBeginning(T elem) {
        SLNode<T> newFirst = new SLNode<>(elem);
        for (int i = 0; i == 0 || r.nextBoolean(); i++) {
            SLNode<T> wasFirst = head.getNextAtLevel(i);
            head.setNextAtLevel(i, newFirst);
            newFirst.setNextAtLevel(i, wasFirst);
        }
    }

    /* TODO could we use addToBeginning instead of this function? */
    private void addWhenEmpty(T elem) {
        SLNode<T> n = new SLNode<>(elem);

        // definitely add the first layer
        head.addLevelPointingTo(n);
        n.addLevelPointingTo(tail);

        // possibly keep adding layers
        while (r.nextBoolean()) {
            head.addLevelPointingTo(n);
            n.addLevelPointingTo(tail);
        }
    }

    /**
     * currently O(N);
     * we could get O(logN) by storing "window" sizes in next-pointers
     */
    @Override public T get(int index) {
        Iterator<T> iterator = iterator();
        while (index-- > 0) iterator.next();
        return iterator.next();
    }

    @Override public boolean contains(Object o) {
        return head.contains((T) o);
    }

    private List<SLNode<T>> floor(T elem) {
        List<SLNode<T>> nodes = head.headFloor(elem);
        if (nodes != null) Collections.reverse(nodes);
        return nodes;
    }
}
