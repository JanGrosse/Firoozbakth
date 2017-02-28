package main;

import java.math.BigInteger;

public class Application {
    public static void main(String... args) {
        SieveOfEratosthenesBigInteger soe = new SieveOfEratosthenesBigInteger(new BigInteger("10000000"));
        soe.calculatePrimes();
        System.out.println(soe.extractPrimes());
        System.out.println("Finished!");
    }
}