package main;

import java.util.ArrayList;
import java.util.BitSet;

public class SieveOfEratosthenes {
    private BitSet bitSet;
    private int maxInt;

    public SieveOfEratosthenes(int maxInt) {
        this.bitSet = new BitSet(maxInt);
        this.bitSet.set(0);
        this.bitSet.set(1);
        this.maxInt = maxInt;
    }

    public void calculatePrimes() {
        int prime = 2;
        int multiplied;
        int lowerBorder;
        while (prime <= maxInt) {
            lowerBorder = prime * prime;
            multiplied = prime;
            //Mark all multiple
            while (multiplied <= maxInt) {
                multiplied += prime;
                if (multiplied >= lowerBorder) {
                    bitSet.set(multiplied);
                }
            }
            //Find next prime
            while (bitSet.get(++prime) && prime <= maxInt) ;
        }
    }

    public ArrayList extractPrimes() {
        ArrayList<Integer> primes = new ArrayList<>();
        for (int i = 0; i < maxInt; i++) {
            if (!bitSet.get(i)) primes.add(i);
        }
        return primes;
    }
}
