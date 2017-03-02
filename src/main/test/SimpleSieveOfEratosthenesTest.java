package main.test;

import junit.framework.TestCase;
import main.SieveOfEratosthenes.ISieveofEratosthenes;
import main.SieveOfEratosthenes.SimpleSieveOfEratosthenes;

import java.math.BigInteger;
import java.util.ArrayList;

public class SimpleSieveOfEratosthenesTest extends TestCase {
    public void testPrimes() throws Exception {
        ISieveofEratosthenes sieve = new SimpleSieveOfEratosthenes(1000000);
        sieve.maskPrimes();
        ArrayList<String> results = sieve.extractPrimes();
        for (int i = 0; i < results.size(); i++) {
            assertTrue(new BigInteger(results.get(i)).isProbablePrime(100));
        }
    }
}