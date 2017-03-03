package main;

public class FiroozPrimesPair {
    private long[] primes;
    private double[] firoozs;

    public FiroozPrimesPair(long[] primes, double[] firoozs) {
        this.primes = primes;
        this.firoozs = firoozs;
    }

    public long[] getPrimes() {
        return primes;
    }

    public double[] getFiroozs() {
        return firoozs;
    }
}
