package price;

import java.util.TreeSet;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class ThreadSafeTreeSet<T extends Comparable<T>> {
    private final TreeSet<T> treeSet;
    private final int maxSize;

    public ThreadSafeTreeSet(int maxSize) {
        this.treeSet = new TreeSet<>();
        this.maxSize = maxSize;
    }

    public synchronized T getSmallest() {
        return treeSet.isEmpty() ? null : treeSet.first();
    }

    public synchronized T getBiggest() {
        return treeSet.isEmpty() ? null : treeSet.last();
    }

    public synchronized boolean add(T element) {
        boolean result = treeSet.add(element);
        trimToMaxSize();
        return result;
    }

    private synchronized void trimToMaxSize() {
        while (treeSet.size() > maxSize)
            treeSet.remove(treeSet.last()); // Removing biggest item
    }

    public synchronized String print() {
        return treeSet.stream()
                .map(Object::toString)
                .collect(Collectors.joining(", "));
    }

    public synchronized Integer checkProperty(BiFunction<TreeSet<T>, T, Integer> function, T item) {
        return function.apply(treeSet, item);
    }

}
