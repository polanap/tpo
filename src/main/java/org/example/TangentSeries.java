package org.example;

public final class TangentSeries {
    private static final double HALF_PI = Math.PI / 2.0;
    private static final double DOMAIN_EPS = 1e-12;

    /**
     * Коэффициенты ряда Маклорена для tan(x):
     * tan(x) = c0*x + c1*x^3 + c2*x^5 + ...
     */
    private static final double[] COEFFICIENTS = {
            1.0,                    // x
            1.0 / 3.0,              // x^3
            2.0 / 15.0,             // x^5
            17.0 / 315.0,           // x^7
            62.0 / 2835.0,          // x^9
            1382.0 / 155925.0,      // x^11
            21844.0 / 6081075.0     // x^13
    };

    private TangentSeries() {
    }

    public static int maxSupportedTerms() {
        return COEFFICIENTS.length;
    }

    /**
     * Вычисляет tan(x) по конечной сумме ряда Маклорена.
     *
     * @param x аргумент в радианах, должен удовлетворять |x| < pi/2
     * @param terms количество членов ряда, от 1 до {@link #maxSupportedTerms()}
     * @return приближенное значение tan(x)
     */
    public static double tanBySeries(double x, int terms) {
        if (terms < 1 || terms > COEFFICIENTS.length) {
            throw new IllegalArgumentException(
                    "Параметр terms должен быть в диапазоне [1, " + COEFFICIENTS.length + "], получено: " + terms
            );
        }
        if (Math.abs(x) >= HALF_PI - DOMAIN_EPS) {
            throw new IllegalArgumentException("Аргумент x должен удовлетворять условию |x| < pi/2 для данного ряда");
        }

        double sum = 0.0;
        double power = x; // Текущая степень x^(2k+1), начинается с x^1 при k = 0
        double x2 = x * x;
        for (int k = 0; k < terms; k++) {
            sum += COEFFICIENTS[k] * power;
            power *= x2;
        }
        return sum;
    }
}
