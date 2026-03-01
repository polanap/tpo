package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TangentSeriesTest {

    @Test
    void returnsZeroAtZero() {
        double actual = TangentSeries.tanBySeries(0.0, TangentSeries.maxSupportedTerms());
        assertEquals(0.0, actual, 1e-15);
    }

    @Test
    void isOddFunction() {
        double x = 0.7;
        int terms = 6;

        double positive = TangentSeries.tanBySeries(x, terms);
        double negative = TangentSeries.tanBySeries(-x, terms);

        assertEquals(-positive, negative, 1e-14);
    }

    @Test
    void matchesMathTanForSmallAndMediumArguments() {
        int terms = TangentSeries.maxSupportedTerms();

        double[] xs = {0.1, -0.3, 0.5, -0.9, 1.0};
        double[] tolerances = {1e-12, 1e-10, 1e-8, 3e-6, 2e-5};

        for (int i = 0; i < xs.length; i++) {
            double expected = Math.tan(xs[i]);
            double actual = TangentSeries.tanBySeries(xs[i], terms);
            assertEquals(expected, actual, tolerances[i], "x = " + xs[i]);
        }
    }

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

    @Test
    void throwsForInvalidTerms() {
        assertThrows(IllegalArgumentException.class, () -> TangentSeries.tanBySeries(0.2, 0));
        assertThrows(
                IllegalArgumentException.class,
                () -> TangentSeries.tanBySeries(0.2, TangentSeries.maxSupportedTerms() + 1)
        );
    }

    @Test
    void throwsAtAndOutsideSingularityBoundary() {
        assertThrows(IllegalArgumentException.class, () -> TangentSeries.tanBySeries(Math.PI / 2.0, 3));
        assertThrows(IllegalArgumentException.class, () -> TangentSeries.tanBySeries(-Math.PI / 2.0, 3));
        assertThrows(IllegalArgumentException.class, () -> TangentSeries.tanBySeries(2.0, 3));
    }
}
