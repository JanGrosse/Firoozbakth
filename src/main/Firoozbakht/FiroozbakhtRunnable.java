package main.Firoozbakht;

import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class FiroozbakhtRunnable extends Thread {
    private List<Long> primes;
    private double[] results;
    private int length;
    private int startingIndex;
    private CyclicBarrier barrier;
    private int id;
    private static int nextId = 0;

    public FiroozbakhtRunnable(List<Long> primes, int startingIndex, CyclicBarrier barrier) {
        this.length = primes.size();
        this.primes = primes;
        this.results = new double[this.length];
        this.startingIndex = startingIndex;
        this.barrier = barrier;
        this.id = ++nextId;
    }

    @Override
    public void run() {
        try {
            int i;
            for (i = 0; i < results.length; i++) {
                results[i] = Math.pow(primes.get(i), 1.0 / (i + startingIndex + 1));
            }
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    public double[] getResults() {
        return results;
    }
}
