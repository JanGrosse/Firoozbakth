package main.PrimeGapsCalculator;

import java.util.ArrayList;

public class PrimeGapsCalculator implements IPrimeGapsCalculator {
    private long[] primes;
    private long[] results;
    private int resultLength;

    public PrimeGapsCalculator(long[] primes) {
        this.primes = primes;
        this.resultLength = primes.length - 1;
        this.results = new long[resultLength];
    }

    @Override
    public void calclatePrimeGaps() {
        for (int i = 0; i < resultLength; i++) {
            results[i] = primes[i + 1] - primes[i] - 1;
        }
    }

    @Override
    public ArrayList<String> getPrimeGaps() {
        ArrayList<String> output = new ArrayList<>();
        for (int i = 0; i < results.length; i++) {
            output.add(Long.toString(results[i]));
        }
        return output;
    }
}
