package org.example;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

public final class TangentSeries {
    private static final double HALF_PI = Math.PI / 2.0;
    private static final double DOMAIN_EPS = 1e-12;
    private static final int MAX_SUPPORTED_TERMS = 12;
    private static final MathContext MC = new MathContext(80, RoundingMode.HALF_EVEN);

    private TangentSeries() {
    }

    public static int maxSupportedTerms() {
        return MAX_SUPPORTED_TERMS;
    }

    /**
     * Вычисляет tan(x) по конечной сумме ряда Маклорена.
     * Коэффициенты вычисляются динамически по формуле через числа Бернулли:
     * c_n = (-1)^(n-1) * 2^(2n) * (2^(2n) - 1) * B_(2n) / (2n)!.
     *
     * @param x аргумент в радианах, должен удовлетворять |x| < pi/2
     * @param terms количество членов ряда, от 1 до {@link #maxSupportedTerms()}
     * @return приближенное значение tan(x)
     */
    public static double tanBySeries(double x, int terms) {
        if (terms < 1 || terms > MAX_SUPPORTED_TERMS) {
            throw new IllegalArgumentException(
                    "Параметр terms должен быть в диапазоне [1, " + MAX_SUPPORTED_TERMS + "], получено: " + terms
            );
        }
        if (Math.abs(x) >= HALF_PI - DOMAIN_EPS) {
            throw new IllegalArgumentException("Аргумент x должен удовлетворять условию |x| < pi/2 для данного ряда");
        }

        BigDecimal[] bernoulli = bernoulliNumbersUpTo(2 * terms);
        double sum = 0.0;
        double power = x; // Текущая степень x^(2n-1), начинается с x^1 при n = 1
        double x2 = x * x;
        for (int n = 1; n <= terms; n++) {
            double coefficient = tangentCoefficient(n, bernoulli[2 * n]);
            sum += coefficient * power;
            power *= x2;
        }
        return sum;
    }

    private static double tangentCoefficient(int n, BigDecimal bernoulli2n) {
        BigInteger twoPower2n = BigInteger.ONE.shiftLeft(2 * n);
        BigDecimal multiplier = new BigDecimal(twoPower2n.multiply(twoPower2n.subtract(BigInteger.ONE)));
        BigDecimal numerator = multiplier.multiply(bernoulli2n, MC);
        if (n % 2 == 0) {
            numerator = numerator.negate();
        }
        BigDecimal denominator = new BigDecimal(factorial(2 * n));
        return numerator.divide(denominator, MC).doubleValue();
    }

    private static BigDecimal[] bernoulliNumbersUpTo(int maxIndex) {
        BigDecimal[] bernoulli = new BigDecimal[maxIndex + 1];
        bernoulli[0] = BigDecimal.ONE;

        for (int m = 1; m <= maxIndex; m++) {
            BigDecimal sum = BigDecimal.ZERO;
            for (int k = 0; k < m; k++) {
                BigDecimal binomial = new BigDecimal(binomial(m + 1, k));
                sum = sum.add(binomial.multiply(bernoulli[k], MC), MC);
            }
            bernoulli[m] = sum.negate().divide(BigDecimal.valueOf(m + 1L), MC);
        }
        return bernoulli;
    }

    private static BigInteger factorial(int n) {
        BigInteger result = BigInteger.ONE;
        for (int i = 2; i <= n; i++) {
            result = result.multiply(BigInteger.valueOf(i));
        }
        return result;
    }

    private static BigInteger binomial(int n, int k) {
        if (k < 0 || k > n) {
            return BigInteger.ZERO;
        }
        int effectiveK = Math.min(k, n - k);
        BigInteger result = BigInteger.ONE;
        for (int i = 1; i <= effectiveK; i++) {
            result = result
                    .multiply(BigInteger.valueOf(n - effectiveK + i))
                    .divide(BigInteger.valueOf(i));
        }
        return result;
    }
}
