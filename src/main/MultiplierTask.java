package main;

public class MultiplierTask implements Runnable{
    private long startPrime;
    private long maxValue;

    public MultiplierTask(long startPrime, long maxValue) {
        this.startPrime = startPrime;
        this.maxValue = maxValue;
    }

    @Override
    public void run() {

    }
}
