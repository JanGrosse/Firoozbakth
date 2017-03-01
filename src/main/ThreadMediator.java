package main;

import java.math.BigInteger;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class ThreadMediator {
    //Holds all bits of finished threads
    private BigBitSet centralBitSet;
    //Supervises the thread count
    private Semaphore threadCreationBarrier;
    //Max amount of threads
    private int maxThreadCount;
    //Holds all threads
    private Thread[] threads;
    //Queue of task which wait on processing
    private Queue<MultiplierTask> taskQueue;
    //Conatins the maxValue
    private long maxValue;
    //Contains the last prime for which a task was created
    private long lastCheckedNumber = 1;
    //Contains the lowest prime of which the multipliers are calculated
    private long lowestWorkingPrime;
    //Contains the squareroot of the max value, which determines the ending condition
    private long upperBarrier;
    //Contains the square of the lowest prime
    private long calculationBarrier = 4;

    public ThreadMediator(int maxThreadCount, long maxValue) {
        this.maxThreadCount = maxThreadCount;
        this.threads = new Thread[maxThreadCount];
        this.threadCreationBarrier = new Semaphore(maxThreadCount);
        this.maxValue = maxValue;
        this.upperBarrier = (long) Math.sqrt(maxValue);
        centralBitSet = new BigBitSet(BigInteger.valueOf(maxValue), true);
        centralBitSet.setBit(0);
        centralBitSet.setBit(1);
        taskQueue = new ArrayDeque<>();
    }

    public void start() {
        createTasks();
        distributeTask();
        while (!taskQueue.isEmpty() || lastCheckedNumber < )
    }

    private void distributeTask() {
        while (threadCreationBarrier < th){

        }
    }

    private void createTasks() {
        while (lastCheckedNumber < calculationBarrier) {
            lastCheckedNumber++;
            if (!centralBitSet.getBit(lastCheckedNumber)) taskQueue.add(new MultiplierTask(lastCheckedNumber, maxValue));
        }
    }
}
