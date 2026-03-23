package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Автоматически регулируемая куча (Skew Heap) для целых чисел.
 * Для тестирования предоставляет трассировку характерных точек алгоритма.
 */
public final class SkewHeap {
    private Node root;
    private int size;

    public enum TracePoint {
        INSERT_START,
        INSERT_END,
        EXTRACT_START,
        EXTRACT_END,
        EXTRACT_EMPTY,
        MERGE_ENTER,
        MERGE_FIRST_NULL,
        MERGE_SECOND_NULL,
        MERGE_SWAP_ROOTS,
        MERGE_RECURSE_RIGHT,
        MERGE_SWAP_CHILDREN,
        MERGE_RETURN_ROOT
    }

    public static final class ExtractTraceResult {
        private final int minValue;
        private final List<TracePoint> trace;

        private ExtractTraceResult(int minValue, List<TracePoint> trace) {
            this.minValue = minValue;
            this.trace = List.copyOf(trace);
        }

        public int minValue() {
            return minValue;
        }

        public List<TracePoint> trace() {
            return trace;
        }
    }

    private static final class Node {
        private final int key;
        private Node left;
        private Node right;

        private Node(int key) {
            this.key = key;
        }
    }

    public int size() {
        return size;
    }

    public int peekMin() {
        if (root == null) {
            throw new NoSuchElementException("Куча пуста");
        }
        return root.key;
    }

    public void insert(int value) {
        root = merge(root, new Node(value), null);
        size++;
    }

    public int extractMin() {
        if (root == null) {
            throw new NoSuchElementException("Куча пуста");
        }
        int min = root.key;
        root = merge(root.left, root.right, null);
        size--;
        return min;
    }

    public List<TracePoint> insertWithTrace(int value) {
        List<TracePoint> trace = new ArrayList<>();
        trace.add(TracePoint.INSERT_START);
        root = merge(root, new Node(value), trace);
        size++;
        trace.add(TracePoint.INSERT_END);
        return List.copyOf(trace);
    }

    public ExtractTraceResult extractMinWithTrace() {
        List<TracePoint> trace = new ArrayList<>();
        trace.add(TracePoint.EXTRACT_START);
        if (root == null) {
            trace.add(TracePoint.EXTRACT_EMPTY);
            throw new NoSuchElementException("Куча пуста");
        }
        int min = root.key;
        root = merge(root.left, root.right, trace);
        size--;
        trace.add(TracePoint.EXTRACT_END);
        return new ExtractTraceResult(min, trace);
    }

    private static Node merge(Node first, Node second, List<TracePoint> trace) {
        addTrace(trace, TracePoint.MERGE_ENTER);
        if (first == null) {
            addTrace(trace, TracePoint.MERGE_FIRST_NULL);
            return second;
        }
        if (second == null) {
            addTrace(trace, TracePoint.MERGE_SECOND_NULL);
            return first;
        }
        if (first.key > second.key) {
            addTrace(trace, TracePoint.MERGE_SWAP_ROOTS);
            Node tmp = first;
            first = second;
            second = tmp;
        }

        addTrace(trace, TracePoint.MERGE_RECURSE_RIGHT);
        first.right = merge(first.right, second, trace);

        addTrace(trace, TracePoint.MERGE_SWAP_CHILDREN);
        Node tmp = first.left;
        first.left = first.right;
        first.right = tmp;

        addTrace(trace, TracePoint.MERGE_RETURN_ROOT);
        return first;
    }

    private static void addTrace(List<TracePoint> trace, TracePoint point) {
        if (trace != null) {
            trace.add(point);
        }
    }
}
