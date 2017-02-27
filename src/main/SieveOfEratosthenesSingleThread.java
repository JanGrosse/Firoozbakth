package main;

import java.math.BigInteger;
import java.util.ArrayList;

public class SieveOfEratosthenesSingleThread {
    //Storage for the markers
    private BigBitSet markers;
    //the max value defined by the user
    private BigBitSet maxValue;
    //the upper border which is defined by the root of of maxValue
    private BigBitSet upperBorder;
    //the current prime of which the multiples are calcualted
    private BigBitSet prime;

    public SieveOfEratosthenesSingleThread(BigInteger maxValueBigInteger) {
        if (maxValueBigInteger.compareTo(new BigInteger("2")) != 1) throw new Error("Maximum must be higher than 2!");
        //To store the markers we use a bitstore the size of the maxValue
        this.markers = new BigBitSet(maxValueBigInteger, true);
        System.out.println(markers.getLongCount() + " Longs as storage!");
        System.out.println(markers.getBitCount().toString() + " Bits as storage!");
        //We set the first two bits, because those are no primes
        this.markers.setBit(0);
        this.markers.setBit(1);

        this.maxValue = new BigBitSet(maxValueBigInteger, false);
        //TODO Check if BigInteger is bigger than max long and sets long max value
        //BigInteger maxLong = BigInteger.valueOf(Long.MAX_VALUE);
        //if (maxLong.compareTo(maxValueBigInteger) == 1) {
        //    this.maxValueLong = maxValueBigInteger.longValue();
        //}
    }

    public void calculatePrimes() {
        BigBitSet multiplied;
        //While prime is smaller then max
        while (prime.compareTo(maxValue) != 1) {
            //Start at square
            multiplied = new BigBitSet(2);//TODO  = prime.multiply(prime);
            //Mark all multiple
            while (multiplied.compareTo(maxValue) != 1) {
                markers.setBit(multiplied.toBigInteger());
                multiplied.add(prime);
            }
            //Find next prime
            prime.increment();
            while (markers.getBit(prime.toBigInteger()) && prime.compareTo(maxValue) != 1) {
                prime.increment();
            }
        }
    }

    public ArrayList extractPrimes() {
        //BigIntegers cause a lot of blocked ram
        System.gc();
        ArrayList<Integer> primes = new ArrayList<>();
        //for (BigBitSet i = maxValue; maxValue.compareTo(i) == 1; i++) {
          //  if (!markers.getBit(i)) primes.add(i);
        //}
        return primes;
    }
}
