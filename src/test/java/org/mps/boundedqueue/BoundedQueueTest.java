package org.mps.boundedqueue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.assertj.core.api.Assertions.*;

class ArrayBoundedQueueTest {

    private BoundedQueue<String> queue;

    @BeforeEach
    void setUp() {
        queue = new ArrayBoundedQueue<>(3);
    }

    @Test
    void shouldInsertElementsInQueue() {
        queue.put("A");
        queue.put("B");

        assertThat(queue.size()).isEqualTo(2);
        assertThat(queue.isEmpty()).isFalse();
        assertThat(queue.isFull()).isFalse();
    }

    @Test
    void shouldThrowWhenInsertingNull() {
        assertThatThrownBy(() -> queue.put(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("null");
    }

    @Test
    void shouldThrowWhenQueueIsFull() {
        queue.put("A");
        queue.put("B");
        queue.put("C");

        assertThat(queue.isFull()).isTrue();

        assertThatThrownBy(() -> queue.put("D"))
                .isInstanceOf(FullBoundedQueueException.class);
    }

    @Test
    void shouldGetElementsInOrder() {
        queue.put("A");
        queue.put("B");
        assertThat(queue.get()).isEqualTo("A");
        assertThat(queue.get()).isEqualTo("B");
        assertThat(queue.size()).isEqualTo(0);
        assertThat(queue.isEmpty()).isTrue();
    }

    @Test
    void shouldThrowWhenGettingFromEmptyQueue() {
        assertThatThrownBy(() -> queue.get())
                .isInstanceOf(EmptyBoundedQueueException.class);
    }

    @Test
    void shouldWrapAroundCorrectlyInCircularBuffer() {
        queue.put("A");
        queue.put("B");
        queue.put("C");

        queue.get(); // remove "A"
        queue.put("D"); // wraps around

        assertThat(queue.get()).isEqualTo("B");
        assertThat(queue.get()).isEqualTo("C");
        assertThat(queue.get()).isEqualTo("D");
    }

    @Test
    void getFirstAndLastShouldReturnCorrectIndices() {
        ArrayBoundedQueue<String> q = new ArrayBoundedQueue<>(3);
        q.put("A");
        assertThat(q.getFirst()).isEqualTo(0);
        assertThat(q.getLast()).isEqualTo(0);

        q.put("B");
        assertThat(q.getLast()).isEqualTo(1);

        q.get(); // removes "A", first = 1
        q.put("C"); // nextFree = 2
        assertThat(q.getFirst()).isEqualTo(1);
        assertThat(q.getLast()).isEqualTo(2);

        q.put("D"); // wraps nextFree to 0
        assertThat(q.getLast()).isEqualTo(0);
    }

    @Test
    void iteratorShouldTraverseElementsInOrder() {
        queue.put("A");
        queue.put("B");
        queue.put("C");

        Iterator<String> iterator = queue.iterator();
        assertThat(iterator).toIterable().containsExactly("A", "B", "C");
    }

    @Test
    void shouldThrowExceptionForInvalidCapacity() {
        assertThatThrownBy(() -> new ArrayBoundedQueue<>(0))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
