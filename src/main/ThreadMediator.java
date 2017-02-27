package main;

import java.math.BigInteger;
import java.util.ArrayDeque;

public class ThreadMediator {
    private int threadCount;
    private BigBitSet centralBitSet;
    private ArrayDeque<SoETask> taskQueue;
    private Thread[] threads;
    //Conatins the maxValue
    private BigInteger maxValue;
    //Contains the lowest prime of which the multipliers are calculated
    private BigInteger lowestWorkingPrime = new BigInteger("2");
    //Contains the square of the lowest prime
    private BigInteger calculationBarrier = new BigInteger("4");

    public ThreadMediator(int threadCount, BigInteger maxValue) {
        this.threadCount = threadCount;
        this.threads = new Thread[threadCount];
        this.maxValue = maxValue;
        centralBitSet = new BigBitSet(maxValue, true);
        taskQueue = new ArrayDeque<>();
    }

    public void start() {
        distributeTask();
        createTasks();
    }

    //TODO
    private void distributeTask() {
        for (int i = 0; i < threadCount; i++) {
           // threads[i] = new Thread().;
        }
    }

    private void createTasks() {
        BigInteger one = new BigInteger("1");
        //Initialize with the follow up number of the prime
        BigInteger i = lowestWorkingPrime.add(one);
        //while the number is smaller than the barrier; find primes and create tasks
        while (i.compareTo(calculationBarrier) == -1) {
            if (!centralBitSet.getBit(i)) {
                taskQueue.add(new SoETask(i, maxValue));
            }
        }
    }
}
