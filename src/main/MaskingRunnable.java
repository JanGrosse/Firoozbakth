package main;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.BrokenBarrierException;

public class MaskingRunnable implements Runnable, Comparable<MaskingRunnable> {
    private long startPrime;
    private long maxValue;
    private BigBitSet bitSet;
    private ThreadMediator mediator;
    private Runnable task;
    //Queue of task which wait on processing
    private final BlockingQueue<Long> waitingTasks;

    public MaskingRunnable(long maxValue, ThreadMediator mediator, BlockingQueue<Long> waitingTasks) {
        this.maxValue = maxValue;
        this.bitSet = new BigBitSet(maxValue);
        this.mediator = mediator;
        this.waitingTasks = waitingTasks;
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
            while (mediator.running || !this.waitingTasks.isEmpty()) {
                this.bitSet.clear();
                this.startPrime = this.waitingTasks.take();
                doMasking();
                mediator.callback(this);
            }
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
