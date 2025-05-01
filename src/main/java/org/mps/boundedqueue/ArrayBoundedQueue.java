package org.mps.boundedqueue;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayBoundedQueue<T> implements BoundedQueue<T> {

    private final T[] buffer;
    private int first;
    private int nextFree;
    private int size;

    @SuppressWarnings("unchecked")
    public ArrayBoundedQueue(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than zero.");
        }
        buffer = (T[]) new Object[capacity];
        first = 0;
        nextFree = 0;
        size = 0;
    }

    @Override
    public void put(T value) {
        if (value == null) {
            throw new IllegalArgumentException("Cannot insert null values.");
        }
        if (isFull()) {
            throw new FullBoundedQueueException("Queue is full.");
        }
        buffer[nextFree] = value;
        nextFree = (nextFree + 1) % buffer.length;
        size++;
    }

    @Override
    public T get() {
        if (isEmpty()) {
            throw new EmptyBoundedQueueException("Queue is empty.");
        }
        T value = buffer[first];
        buffer[first] = null; // help GC
        first = (first + 1) % buffer.length;
        size--;
        return value;
    }

    @Override
    public boolean isFull() {
        return size == buffer.length;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public int getFirst() {
        return first;
    }

    @Override
    public int getLast() {
        return (nextFree - 1 + buffer.length) % buffer.length;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < size;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                int realIndex = (first + index) % buffer.length;
                index++;
                return buffer[realIndex];
            }
        };
    }
}
