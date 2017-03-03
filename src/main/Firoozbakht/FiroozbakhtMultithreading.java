package main.Firoozbakht;

import java.util.ArrayList;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class FiroozbakhtMultithreading implements Firoozbakht {
    private ArrayList<Long> primes;
    private FiroozbakhtRunnable[] threads;
    private double[] tempResults;
    private int length;
    private int threadLoad;
    private boolean isProcessed = false;
    private int threadCount;
    private CyclicBarrier finalBarrier;

    public FiroozbakhtMultithreading(long[] primes, int threadCount) {
        this.primes = toArrayList(primes);
        this.length = primes.length;
        this.tempResults = new double[length];
        this.threadCount = threadCount;
        this.finalBarrier = new CyclicBarrier(threadCount + 1);
        this.threads = new FiroozbakhtRunnable[threadCount];
    }

    private ArrayList<Long> toArrayList(long[] primes) {
        ArrayList<Long> list = new ArrayList<>();
        for (long prime : primes) {
            list.add(prime);
        }
        return list;
    }

    public void processPrimes() {
        try {
            this.threadLoad = length / threadCount;
            int start, i;
            for (i = 0; i < threadCount - 1; i++) {
                start = i * threadLoad;
                threads[i] = new FiroozbakhtRunnable(primes.subList(start, (i + 1) * threadLoad), start, this.finalBarrier);
            }
            start = (i) * threadLoad;
            threads[i] = new FiroozbakhtRunnable(primes.subList(start, primes.size() - 1), start, this.finalBarrier);
            startThreads();
            this.finalBarrier.await();
            joinResults();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    private void joinResults() {
        double[] temp;
        int index = 0;
        for (FiroozbakhtRunnable thread : this.threads) {
            temp = thread.getResults();
            for (double d : temp) {
                tempResults[index++] = d;
            }
        }
        isProcessed = true;
    }

    public void startThreads() {
        for (FiroozbakhtRunnable thread : this.threads) {
            thread.start();
        }
    }

    public void checkFalsify() {
        if (!isProcessed) throw new Error("You have to process the primes first!");
        int i;
        for (i = 1; i < tempResults.length; i++) {
            if (tempResults[i - 1] <= tempResults[i]) {
                System.out.println("Firoozbakht was falsified for prime " + (i - 1) + ":" + tempResults[i - 1] + " and prime " + (i) + ":" + tempResults[i]);
            }
        }
        System.out.println("Firoozbakht could not be falsified!");
        System.out.println(tempResults.length + " primes were checked!");
    }

    @Override
    public ArrayList<String> getFiroozbakhtResults() {
        ArrayList<String> firoozs = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            firoozs.add(Double.toString(tempResults[i]));
        }
        return firoozs;
    }

}
