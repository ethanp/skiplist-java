package skiplist;

import java.util.ArrayList;
import java.util.List;

/**
 * 8/26/16 9:24 PM
 */
class SLNode<T extends Comparable<T>> {
    private final T elem;
    private List<SLNode<T>> nextPointers = new ArrayList<>();

    SLNode(T elem) {
        this.elem = elem;
    }

    T getElem() {
        return elem;
    }

    SLNode<T> floor(T needle) {
        if (elem.compareTo(needle) == 0) {
            return this;
        } else if (elem.compareTo(needle) > 0) {
            return null;
        }
        assert elem.compareTo(needle) < 0;
        for (int i = nextPointers.size() - 1; i >= 0; i--) {
            SLNode<T> next = nextPointers.get(i);
            if (next.getElem().compareTo(needle) <= 0) {
                return next;
            }
        }
        return this;
    }

    void setNextAtLevel(int i, SLNode<T> newNext) {
        if (i > nextPointers.size()) {
            throw new IllegalArgumentException(
                i + " is out of range " + nextPointers.size()
            );
        } else if (i == nextPointers.size()) {
            addLevelPointingTo(newNext);
        }
        assert i < nextPointers.size();
        nextPointers.set(i, newNext);
    }

    void addLevelPointingTo(SLNode<T> next) {
        nextPointers.add(next);
    }

    /** does not check if the level is in-bounds */
    SLNode<T> getNextAtLevel(int level) {
        return nextPointers.get(level);
    }

    /**
     * special version of the "floor(T)" for being run on the list head
     *
     * Returns null if the list is empty.
     * Returns empty list if there are no less or equal elements.
     * Otw returns a list of prev nodes for all levels.
     */
    List<SLNode<T>> headFloor(T elem) {
        if (nextPointers.isEmpty()) return null;
        int sparsestList = nextPointers.size() - 1;
        return this.floorFromLevel(sparsestList, elem);
    }

    private List<SLNode<T>> floorFromLevel(int i, T elem) {
        ArrayList<SLNode<T>> ret = new ArrayList<>();
        for (; i >= 0; i--) {
            T nextElem = nextPointers.get(i).getElem();
            if (nextElem != null && elem.compareTo(nextElem) > 0) break;
            ret.add(this);
        }
        if (i >= 0) ret.addAll(nextPointers.get(i).floorFromLevel(i, elem));
        return ret;
    }

    boolean contains(T e) {
        return this.containsStartingAtLevel(nextPointers.size() - 1, e);
    }

    private boolean containsStartingAtLevel(int i, T e) {
        for (; i >= 0; i--) {
            SLNode<T> nextNode = nextPointers.get(i);
            if (nextNode.getElem() == null) {/*continue;*/
            } else if (nextNode.getElem().compareTo(e) == 0) {
                return true;
            } else if (nextNode.getElem().compareTo(e) < 0) {
                return nextNode.containsStartingAtLevel(i, e);
            } /* else if next is greater than 0, continue iterating */
        }
        return false;
    }
}
