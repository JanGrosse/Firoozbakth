package main.Firoozbakht;

import java.util.ArrayList;

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
    public void checkFalsify() {
        if (!isProcessed) throw new Error("You have to process the primes first!");
        for (int i = 1; i < length; i++) {
            if (results[i - 1] <= results[i]) {
                System.out.println("Firoozbakht was falsified for prime " + (i - 1) + ": " + results[i - 1] + " and prime " + (i) + ": " + results[i]);
            }
        }
        System.out.println("Firoozbakht could not be falsified!");
    }

    @Override
    public ArrayList<String> getFiroozbakhtResults() {
        ArrayList<String> firoozs = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            firoozs.add(Double.toString(results[i]));
        }
        return firoozs;
    }

}
