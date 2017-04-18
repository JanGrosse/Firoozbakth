package main.SieveOfEratosthenes;

import main.DataStructures.BigBitSet;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.BrokenBarrierException;

public class MaskingRunnable extends Thread implements Comparable<MaskingRunnable> {
    private static int nextID = 1;
    private int id;
    private long startPrime;
    private long maxValue;
    private BigBitSet bitSet;
    private SieveOfEratosthenesMultiThread mediator;
    private Runnable task;
    //Queue of task which wait on processing
    private final BlockingQueue<Long> waitingTasks;

    public MaskingRunnable(long maxValue, SieveOfEratosthenesMultiThread mediator, BlockingQueue<Long> waitingTasks) {
        this.maxValue = maxValue;
        this.bitSet = new BigBitSet(maxValue);
        this.mediator = mediator;
        this.waitingTasks = waitingTasks;
        this.id = nextID++;
    }

    public BigBitSet getBitSet() {
        return bitSet;
    }

    public long getStartPrime() {
        return startPrime;
    }

    @Override
    public void run() {
        try {
            while (true) {
                this.bitSet.clear();
                this.startPrime = this.waitingTasks.take();
                if (startPrime == 0) break;
                mediator.setTaskActive(this.startPrime);
                doMasking();
                mediator.callback(this);
            }
            System.out.println("Thread "+ id + " finished!");
            this.mediator.checkout();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    private void doMasking() {
        long multiplied = startPrime * startPrime;
        while (multiplied <= maxValue) {
            bitSet.setBit(multiplied);
            multiplied += startPrime;
        }
    }

    /**
     * will sort smallest first
     *
     * @param o Task to compare to
     * @return result
     */
    @Override
    public int compareTo(MaskingRunnable o) {
        if (o.getStartPrime() == startPrime) return 0;
        return startPrime < o.getStartPrime() ? 1 : -1;
    }
}
