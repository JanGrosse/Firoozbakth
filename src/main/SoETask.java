package main;

import java.math.BigInteger;

public class SoETask implements Runnable{
    private BigInteger startPrime;
    private BigInteger maxValue;

    public SoETask(BigInteger startPrime, BigInteger maxValue) {
        this.startPrime = startPrime;
        this.maxValue = maxValue;
    }

    @Override
    public void run() {

    }
}
