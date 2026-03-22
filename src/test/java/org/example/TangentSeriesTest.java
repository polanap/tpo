package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TangentSeriesTest {
    private static final double DEFAULT_ACCURACY = 1e-6;

    @DisplayName("<tg> Тест #1: tg(0) равен 0")
    @Test
    void returnsZeroAtZero() {
        double actual = TangentSeries.tanBySeries(0.0, DEFAULT_ACCURACY);
        assertEquals(0.0, actual, 1e-15);
    }

    @DisplayName("<tg> Тест #2: tg(x) — нечетная функция")
    @Test
    void isOddFunction() {
        double x = 0.7;
        double accuracy = 1e-6;

        double positive = TangentSeries.tanBySeries(x, accuracy);
        double negative = TangentSeries.tanBySeries(-x, accuracy);

        assertEquals(-positive, negative, accuracy);
    }

    @DisplayName("<tg> Тест #3: Ряд Маклорена аппроксимирует Math.tan на малых и средних x")
    @Test
    void matchesMathTanForSmallAndMediumArguments() {
        double[] xs = {-0.7, -0.5, -0.3, 0.3, 0.5, 0.7};

        for (int i = 0; i < xs.length; i++) {
            double expected = Math.tan(xs[i]);
            double actual = TangentSeries.tanBySeries(xs[i], DEFAULT_ACCURACY);
            assertEquals(expected, actual, DEFAULT_ACCURACY, "x = " + xs[i]);
        }
    }


    @DisplayName("<tg> Тест #4: Увеличение точности улучшает аппроксимацию")
    @Test
    void stricterAccuracyImprovesOrKeepsApproximation() {
        double x = 0.9;
        double expected = Math.tan(x);

        double coarseError = Math.abs(expected - TangentSeries.tanBySeries(x, 1e-3));
        double mediumError = Math.abs(expected - TangentSeries.tanBySeries(x, 1e-4));
        double strictError = Math.abs(expected - TangentSeries.tanBySeries(x, 1e-5));

        assertTrue(mediumError <= coarseError);
        assertTrue(strictError <= mediumError);
    }

    @DisplayName("<tg> Тест #5: Некорректная точность вызывает исключение")
    @Test
    void throwsForTooLargeAccuracy() {
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () -> TangentSeries.tanBySeries(0.2, 0.0));
        IllegalArgumentException ex2 = assertThrows(
                IllegalArgumentException.class,
                () -> TangentSeries.tanBySeries(0.2, -1e-6)
        );
        IllegalArgumentException ex3 = assertThrows(
                IllegalArgumentException.class,
                () -> TangentSeries.tanBySeries(0.2, Double.NaN)
        );
        assertTrue(ex1.getMessage().contains("accuracy"));
        assertTrue(ex2.getMessage().contains("accuracy"));
        assertTrue(ex3.getMessage().contains("accuracy"));
    }

    @DisplayName("<tg> Тест #6: На границе и вне области |x| < pi/2 выбрасывается исключение")
    @Test
    void throwsAtAndOutsideSingularityBoundary() {
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () -> TangentSeries.tanBySeries(Math.PI / 2.0, DEFAULT_ACCURACY));
        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () -> TangentSeries.tanBySeries(-Math.PI / 2.0, DEFAULT_ACCURACY));
        IllegalArgumentException ex3 = assertThrows(IllegalArgumentException.class, () -> TangentSeries.tanBySeries(2.0, DEFAULT_ACCURACY));
        assertTrue(ex1.getMessage().contains("|x| < pi/2"));
        assertTrue(ex2.getMessage().contains("|x| < pi/2"));
        assertTrue(ex3.getMessage().contains("|x| < pi/2"));
    }

    @DisplayName("<tg> Тест #7: Если нужная точность недостижима за MAX_SUPPORTED_TERMS, выбрасывается исключение")
    @Test
    void throwsWhenAccuracyNotReachedByMaxTerms() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> TangentSeries.tanBySeries(1.2, 1e-12)
        );
        assertTrue(ex.getMessage().contains("Не удалось достичь требуемой точности"));
    }
}
