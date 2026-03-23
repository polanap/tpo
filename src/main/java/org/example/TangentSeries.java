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

    /**
     * Вычисляет tan(x) по конечной сумме ряда Маклорена.
     * Коэффициенты вычисляются динамически по формуле через числа Бернулли:
     * c_n = (-1)^(n-1) * 2^(2n) * (2^(2n) - 1) * B_(2n) / (2n)!.
     *
     * @param x аргумент в радианах, должен удовлетворять |x| < pi/2
     * @param accuracy требуемая абсолютная точность
     * @return приближенное значение tan(x)
     */
    public static double tanBySeries(double x, double accuracy) {
        if (!Double.isFinite(accuracy) || accuracy <= 0.0) {
            throw new IllegalArgumentException("Параметр accuracy должен быть положительным конечным числом");
        }
        if (Math.abs(x) >= HALF_PI - DOMAIN_EPS) {
            throw new IllegalArgumentException("Аргумент x должен удовлетворять условию |x| < pi/2 для данного ряда");
        }

        BigDecimal[] bernoulli = bernoulliNumbersUpTo(2 * MAX_SUPPORTED_TERMS);
        double sum = 0.0;
        double power = x; // Текущая степень x^(2n-1), начинается с x^1 при n = 1
        double x2 = x * x;
        for (int n = 1; n <= MAX_SUPPORTED_TERMS; n++) {
            double coefficient = tangentCoefficient(n, bernoulli[2 * n]);
            double term = coefficient * power;
            sum += term;
            if (Math.abs(term) <= accuracy) {
                return sum;
            }
            power *= x2;
        }
        throw new IllegalArgumentException(
                "Не удалось достичь требуемой точности " + accuracy
                        + " за " + MAX_SUPPORTED_TERMS + " членов ряда"
        );
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
