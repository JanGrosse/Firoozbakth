package main.Firoozbakht;

public class SimpleFiroozbakht implements Firoozbakht {
    private long[] primes;
    private double[] results;
    private boolean isProcessed = false;
    private int length;

    public SimpleFiroozbakht(long[] primes) {
        this.primes = primes;
        this.length = primes.length;
        this.results = new double[this.length];
    }


    @Override
    public void processPrimes() {
        for (int i = 0; i < length; i++) {
            results[i] = Math.pow(primes[i], 1.0 / (i + 1));
        }
        isProcessed = true;
    }

    @Override
    public long checkFalsify() {
        if (!isProcessed) throw new Error("You have to process the primes first!");
        for (int i = 1; i < length; i++) {
            if (results[i - 1] <= results[i]) {
                System.out.println("Firoozbakht was falsified for prime " + (i - 1) + ": " + results[i - 1] + " and prime " + (i) + ": " + results[i]);
                return i;
            }
        }
        System.out.println("Firoozbakht could not be falsified!");
        return 0;
    }

    @Override
    public double[] getResults() {
        return results;
    }
}
