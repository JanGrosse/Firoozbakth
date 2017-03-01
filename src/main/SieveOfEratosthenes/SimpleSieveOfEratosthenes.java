package main.SieveOfEratosthenes;

import main.BigBitSet;

import java.math.BigInteger;
import java.util.ArrayList;

public class SimpleSieveOfEratosthenes implements ISieveofEratosthenes {
    private BigBitSet bitSet;
    private long maxValue;
    private long upperBarrier;

    public SimpleSieveOfEratosthenes(long maxValue) {
        this.bitSet = new BigBitSet(BigInteger.valueOf(maxValue), true);
        this.bitSet.setBit(0);
        this.bitSet.setBit(1);
        this.maxValue = maxValue;
        this.upperBarrier = (long) Math.sqrt(maxValue);
    }

    public void maskPrimes() {
        long prime = 2;
        long multiplied;
        while (prime <= upperBarrier) {
            //Start at square
            multiplied = prime * prime;
            //Mark all multiple
            while (multiplied <= maxValue) {
                bitSet.setBit(multiplied);
                multiplied += prime;
            }
            //Find next prime
            while (bitSet.getBit(++prime) && prime <= upperBarrier) ;
        }
    }

    public ArrayList<String> extractPrimes() {
        ArrayList<String> primes = new ArrayList<>();
        for (long i = 0; i < maxValue; i++) {
            if (!bitSet.getBit(i)) primes.add(Long.toString(i));
        }
        return primes;
    }

    public BigBitSet getBitSet() {
        return bitSet;
    }
}
