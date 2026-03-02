package org.example;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SkewHeapTest {

    @DisplayName("Вставка в пустую кучу: трасса совпадает с эталоном")
    @Test
    void traceForInsertIntoEmptyHeapMatchesReference() {
        SkewHeap heap = new SkewHeap();

        List<SkewHeap.TracePoint> actual = heap.insertWithTrace(5);
        List<SkewHeap.TracePoint> expected = List.of(
                SkewHeap.TracePoint.INSERT_START,
                SkewHeap.TracePoint.MERGE_ENTER,
                SkewHeap.TracePoint.MERGE_FIRST_NULL,
                SkewHeap.TracePoint.INSERT_END
        );

        assertEquals(expected, actual);
        assertEquals(1, heap.size());
        assertEquals(5, heap.peekMin());
    }

    @DisplayName("Вставка с перестановкой корней: трасса совпадает с эталоном")
    @Test
    void traceForInsertWithRootSwapMatchesReference() {
        SkewHeap heap = new SkewHeap();
        heap.insert(5);

        List<SkewHeap.TracePoint> actual = heap.insertWithTrace(3);
        List<SkewHeap.TracePoint> expected = List.of(
                SkewHeap.TracePoint.INSERT_START,
                SkewHeap.TracePoint.MERGE_ENTER,
                SkewHeap.TracePoint.MERGE_SWAP_ROOTS,
                SkewHeap.TracePoint.MERGE_RECURSE_RIGHT,
                SkewHeap.TracePoint.MERGE_ENTER,
                SkewHeap.TracePoint.MERGE_FIRST_NULL,
                SkewHeap.TracePoint.MERGE_SWAP_CHILDREN,
                SkewHeap.TracePoint.MERGE_RETURN_ROOT,
                SkewHeap.TracePoint.INSERT_END
        );

        assertEquals(expected, actual);
        assertEquals(2, heap.size());
        assertEquals(3, heap.peekMin());
    }

    @DisplayName("Извлечение минимума: трасса совпадает с эталоном")
    @Test
    void traceForExtractMinMatchesReference() {
        SkewHeap heap = new SkewHeap();
        heap.insert(3);
        heap.insert(5);

        SkewHeap.ExtractTraceResult actual = heap.extractMinWithTrace();
        List<SkewHeap.TracePoint> expectedTrace = List.of(
                SkewHeap.TracePoint.EXTRACT_START,
                SkewHeap.TracePoint.MERGE_ENTER,
                SkewHeap.TracePoint.MERGE_SECOND_NULL,
                SkewHeap.TracePoint.EXTRACT_END
        );

        assertEquals(3, actual.minValue());
        assertEquals(expectedTrace, actual.trace());
        assertEquals(1, heap.size());
        assertEquals(5, heap.peekMin());
    }

    @DisplayName("Последовательность извлечения отсортирована для смешанных данных")
    @Test
    void extractionOrderIsSortedForMixedInputData() {
        SkewHeap heap = new SkewHeap();
        int[] data = {7, 1, 9, 3, 3, 2, 8};
        for (int value : data) {
            heap.insert(value);
        }

        int[] expected = {1, 2, 3, 3, 7, 8, 9};
        for (int expectedMin : expected) {
            assertEquals(expectedMin, heap.extractMin());
        }
        assertEquals(0, heap.size());
        NoSuchElementException ex = assertThrows(NoSuchElementException.class, heap::extractMin);
        assertEquals("Куча пуста", ex.getMessage());
    }
}
