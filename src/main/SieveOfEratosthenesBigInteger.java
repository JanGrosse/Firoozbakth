package main;

import java.math.BigInteger;
import java.util.ArrayList;

public class SieveOfEratosthenesBigInteger {
    //private BigBitSet bitSet;
    private BigInteger bitSet = new BigInteger("0");
    private BigInteger maxInt;

    public SieveOfEratosthenesBigInteger(BigInteger maxInt) {
        //this.bitSet = new BigBitSet(maxInt.intValue() * 64);
        bitSet = bitSet.setBit(0);
        bitSet = bitSet.setBit(1);
        this.maxInt = maxInt;
    }

    public void calculatePrimes() {
        BigInteger one = new BigInteger("1");
        BigInteger prime = new BigInteger("2");
        BigInteger multiplied;
        while (prime.compareTo(maxInt) != 1) {
            //Start at square
            multiplied = prime.multiply(prime);
            //Mark all multiple
            while (multiplied.compareTo(maxInt) != 1) {
                bitSet = bitSet.setBit(multiplied.intValue());
                multiplied = multiplied.add(prime);
            }
            //Find next prime
            prime = prime.add(one);
            while (bitSet.testBit(prime.intValue()) && prime.compareTo(maxInt) != 1) prime = prime.add(one);
        }

    }

    public void setBit() {

    }

    public ArrayList extractPrimes() {
        ArrayList<Integer> primes = new ArrayList<>();
        for (int i = 0; maxInt.compareTo(BigInteger.valueOf(i)) == 1; i++) {
            if (!bitSet.testBit(i)) primes.add(i);
        }
        return primes;
    }
}
