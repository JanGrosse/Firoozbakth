package main.SieveOfEratosthenes;

import main.Configuration;
import main.DataStructures.BigBitSet;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingQueue;

public class SieveOfEratosthenesMultiThread implements ISieveOfEratosthenes {
    //For thread syncing
    private static final Object lock = new Object();
    //For waiting the last threads to finish
    private CyclicBarrier finalBarrier;
    //Informing the threads to shut down after they finished their task
    private boolean running;
    //Holds all bits of finished threads
    private BigBitSet centralBitSet;
    //Max amount of threads
    private int maxThreadCount;
    //Holds all currently working tasks
    private ArrayList<Long> activeTasks;
    //thw highest finished multiplier task
    private long finishedTask;
    //Queue of task which wait on processing
    private final BlockingQueue<Long> waitingTasks;
    //Conatins the maxValue
    private long maxValue;
    //Contains the last prime for which a task was created
    private long lastCheckedNumber = 3;
    //Contains the lowest prime of which the multipliers are calculated
    private long lowestWorkingPrime = 2;
    //Contains the squareroot of the max value, which determines the ending condition
    private long upperBarrier;
    //Contains the square of the lowest prime
    private long calculationBarrier;

    public SieveOfEratosthenesMultiThread(int maxThreadCount, long maxValue) {
        this.finalBarrier = new CyclicBarrier(maxThreadCount + 1);
        this.running = true;
        this.centralBitSet = new BigBitSet(BigInteger.valueOf(maxValue), true);
        this.centralBitSet.setBit(0);//0 is not a prime
        this.centralBitSet.setBit(1);//1 is not a prime
        this.maxThreadCount = maxThreadCount;
        this.activeTasks = new ArrayList<>();
        this.waitingTasks = new LinkedBlockingQueue<>();
        this.maxValue = maxValue;
        this.lastCheckedNumber = 1;
        this.upperBarrier = (long) Math.sqrt(maxValue);
    }

    public SieveOfEratosthenesMultiThread() {
        this(Configuration.instance.threadCount, Configuration.instance.findPrimesUpTo);
    }

    private void createThreads() {
        synchronized (lock) {
            for (int i = 0; i < maxThreadCount; i++) {
                new MaskingRunnable(this.maxValue, this, this.waitingTasks).start();
            }
        }
    }

    public void setTaskActive(long prime) {
        synchronized (lock) {
            this.activeTasks.add(prime);
            if (activeTasks.size() == 1) {
                changeCalculationBarrier(prime);
            }
        }
    }

    public void callback(MaskingRunnable task) {
        synchronized (lock) {
            //Merge BitSets
            this.centralBitSet.binaryOr(task.getBitSet());
            //Remove from list
            activeTasks.remove(task.getStartPrime());
            //If waiting for finishing
            if (!running) return;
            //Check if new Tasks can be created
            if (task.getStartPrime() == lowestWorkingPrime) {
                if (!activeTasks.isEmpty()) {
                    //Determine new lowest working thread
                    Collections.sort(activeTasks);
                    changeCalculationBarrier(activeTasks.get(0));
                } else {
                    changeCalculationBarrier(finishedTask);
                }
            } else {
                long prime = task.getStartPrime();
                if (prime > finishedTask) finishedTask = prime;
            }
        }
    }

    private void changeCalculationBarrier(Long prime) {
        lowestWorkingPrime = prime;
        calculationBarrier = lowestWorkingPrime * lowestWorkingPrime;
        createTasks();
    }

    private void createTasks() {
        while (lastCheckedNumber < calculationBarrier && lastCheckedNumber < upperBarrier) {
            lastCheckedNumber++;
            if (!centralBitSet.getBit(lastCheckedNumber))
                waitingTasks.add(lastCheckedNumber);
        }
        if (lastCheckedNumber == upperBarrier) initEnd();
    }

    private void initEnd() {
        this.running = false;
        //Add death message
        for (int i = 0; i < maxThreadCount; i++) {
            waitingTasks.add(0L);
        }
    }

    public void checkout() throws BrokenBarrierException, InterruptedException {
        this.finalBarrier.await();
    }

    @Override
    public void maskPrimes() {
        try {
            changeCalculationBarrier(2L);
            createThreads();
            this.finalBarrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<String> extractPrimes() {
        ArrayList<String> primes = new ArrayList<>();
        for (long i = 0; i < maxValue; i++) {
            if (!centralBitSet.getBit(i)) primes.add(Long.toString(i));
        }
        return primes;
    }
}
