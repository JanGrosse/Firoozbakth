package main.SieveOfEratosthenes;

import main.BigBitSet;

import java.math.BigInteger;
import java.util.ArrayList;

public class SieveOfEratosthenesBigInteger implements ISieveofEratosthenes {
    //private BigBitSet bitSet;
    private BigBitSet bitSet;
    private BigInteger maxValue;

    public SieveOfEratosthenesBigInteger(BigInteger maxValue) {
        this.bitSet = new BigBitSet(maxValue, true);
        bitSet.setBit(0);
        bitSet.setBit(1);
        this.maxValue = maxValue;
    }

    public void maskPrimes() {
        BigInteger one = new BigInteger("1");
        BigInteger prime = new BigInteger("2");
        BigInteger barrier = sqrt(maxValue);
        BigInteger multiplied;
        while (prime.compareTo(barrier) != 1) {
            //Start at square
            multiplied = prime.multiply(prime);
            //Mark all multiple
            while (multiplied.compareTo(maxValue) != 1) {
                bitSet.setBit(multiplied.intValue());
                multiplied = multiplied.add(prime);
            }
            //Find next prime
            prime = prime.add(one);
            while (bitSet.getBit(prime) && prime.compareTo(maxValue) != 1) prime = prime.add(one);
        }

    }

    public ArrayList<String> extractPrimes() {
        ArrayList<String> primes = new ArrayList<>();
        for (long i = 0; maxValue.compareTo(BigInteger.valueOf(i)) == 1; i++) {
            if (!bitSet.getBit(i)) primes.add(Long.toString(i));
        }
        return primes;
    }

    public BigInteger sqrt(BigInteger n) {
        BigInteger a = BigInteger.ONE;
        BigInteger b = new BigInteger(n.shiftRight(5).add(new BigInteger("8")).toString());
        while (b.compareTo(a) >= 0) {
            BigInteger mid = new BigInteger(a.add(b).shiftRight(1).toString());
            if (mid.multiply(mid).compareTo(n) > 0) b = mid.subtract(BigInteger.ONE);
            else a = mid.add(BigInteger.ONE);
        }
        return a.subtract(BigInteger.ONE);
    }
}
