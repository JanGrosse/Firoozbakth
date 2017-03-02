package main;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingQueue;

public class ThreadMediator {
    //For thread syncing
    static final Object lock = new Object();
    //For waiting the last threads to finish
    private CyclicBarrier finalBarrier;
    //Informing the threads to shut down after they finished their task
    volatile boolean running;
    //Holds all bits of finished threads
    private BigBitSet centralBitSet;
    //Max amount of threads
    private int maxThreadCount;
    //Holds all currently working tasks
    private ArrayList<Long> activeTasks;
    //Queue of task which wait on processing
    private final BlockingQueue<Long> waitingTasks;
    //Conatins the maxValue
    private long maxValue;
    //Contains the last prime for which a task was created
    private long lastCheckedNumber;
    //Contains the lowest prime of which the multipliers are calculated
    private long lowestWorkingPrime;
    //Contains the squareroot of the max value, which determines the ending condition
    private long upperBarrier;
    //Contains the square of the lowest prime
    private long calculationBarrier;

    public ThreadMediator(int maxThreadCount, long maxValue) {
        this.finalBarrier = new CyclicBarrier(maxThreadCount);
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

    public void start() {
        try {
            createInitialTasks();
            createThreads();
            this.finalBarrier.await();
            this.end();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    private void createThreads() {
        for (int i = 0; i < maxThreadCount; i++) {
            new Thread(new MaskingRunnable(this.maxValue, this, this.waitingTasks)).run();
        }
    }

    public void callback(MaskingRunnable task) {
        synchronized (lock) {
            //Merge BitSets
            this.centralBitSet.binaryOr(task.getBitSet());
            //TODO remove. Only for dev
            System.out.println(this.centralBitSet);
            //Remove from list
            activeTasks.remove(task.getStartPrime());
            //Check if new Tasks can be created
            if (task.getStartPrime() == lowestWorkingPrime && this.running) {
                //Determine new lowest working thread
                Collections.sort(activeTasks);
                lowestWorkingPrime = activeTasks.get(0);
                calculationBarrier = lowestWorkingPrime * lowestWorkingPrime;
                createTasks();
            }
        }
    }

    private void end() {
        System.out.println("Finished!");
    }

    private void createInitialTasks() {
        this.calculationBarrier = 4;
        this.waitingTasks.add(2L);
        this.waitingTasks.add(3L);
    }

    private void createTasks() {
        while (lastCheckedNumber < calculationBarrier) {
            lastCheckedNumber++;
            if (!centralBitSet.getBit(lastCheckedNumber))
                waitingTasks.add(lastCheckedNumber);
        }
        if (lastCheckedNumber == upperBarrier) this.running = false;
    }

    public void checkout() throws BrokenBarrierException, InterruptedException {
        this.finalBarrier.await();
    }
}
