package main;

import java.math.BigInteger;

public class Application {
    public static void main(String... args) {
        SieveOfEratosthenesSingleThread soe = new SieveOfEratosthenesSingleThread(new BigInteger("19199999"));
        soe.calculatePrimes();
        System.out.println(soe.extractPrimes());
        System.out.println("Finished!");
    }
}