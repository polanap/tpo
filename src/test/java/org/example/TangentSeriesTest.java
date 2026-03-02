package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TangentSeriesTest {

    @DisplayName("tg(0) равен 0")
    @Test
    void returnsZeroAtZero() {
        double actual = TangentSeries.tanBySeries(0.0, TangentSeries.maxSupportedTerms());
        assertEquals(0.0, actual, 1e-15);
    }

    @DisplayName("tg(x) — нечетная функция")
    @Test
    void isOddFunction() {
        double x = 0.7;
        int terms = 6;

        double positive = TangentSeries.tanBySeries(x, terms);
        double negative = TangentSeries.tanBySeries(-x, terms);

        assertEquals(-positive, negative, 1e-14);
    }

    @DisplayName("Ряд Маклорена аппроксимирует Math.tan на малых и средних x")
    @Test
    void matchesMathTanForSmallAndMediumArguments() {
        int terms = TangentSeries.maxSupportedTerms();

        double[] xs = {0.1, -0.3, 0.5, -0.7};
        double[] tolerances = {1e-12, 1e-10, 1e-7, 1e-5};

        for (int i = 0; i < xs.length; i++) {
            double expected = Math.tan(xs[i]);
            double actual = TangentSeries.tanBySeries(xs[i], terms);
            assertEquals(expected, actual, tolerances[i], "x = " + xs[i]);
        }
    }

    @DisplayName("Увеличение числа членов ряда повышает точность у границы сходимости")
    @Test
    void moreTermsImproveApproximationNearConvergenceRadius() {
        double x = 1.2;
        double expected = Math.tan(x);

        double errorWith3 = Math.abs(expected - TangentSeries.tanBySeries(x, 3));
        double errorWith5 = Math.abs(expected - TangentSeries.tanBySeries(x, 5));
        double errorWith7 = Math.abs(expected - TangentSeries.tanBySeries(x, 7));

        assertTrue(errorWith5 < errorWith3);
        assertTrue(errorWith7 < errorWith5);
    }

    @DisplayName("Некорректное число членов ряда вызывает исключение")
    @Test
    void throwsForInvalidTerms() {
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () -> TangentSeries.tanBySeries(0.2, 0));
        IllegalArgumentException ex2 = assertThrows(
                IllegalArgumentException.class,
                () -> TangentSeries.tanBySeries(0.2, TangentSeries.maxSupportedTerms() + 1)
        );
        assertTrue(ex1.getMessage().contains("terms"));
        assertTrue(ex2.getMessage().contains("terms"));
    }

    @DisplayName("На границе и вне области |x| < pi/2 выбрасывается исключение")
    @Test
    void throwsAtAndOutsideSingularityBoundary() {
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () -> TangentSeries.tanBySeries(Math.PI / 2.0, 3));
        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () -> TangentSeries.tanBySeries(-Math.PI / 2.0, 3));
        IllegalArgumentException ex3 = assertThrows(IllegalArgumentException.class, () -> TangentSeries.tanBySeries(2.0, 3));
        assertTrue(ex1.getMessage().contains("|x| < pi/2"));
        assertTrue(ex2.getMessage().contains("|x| < pi/2"));
        assertTrue(ex3.getMessage().contains("|x| < pi/2"));
    }
}
