package main.Tests;

import junit.framework.TestCase;
import main.SieveOfEratosthenes.ISieveOfEratosthenes;
import main.SieveOfEratosthenes.SieveOfEratosthenesMultiThread;
import main.SieveOfEratosthenes.SimpleSieveOfEratosthenes;

import java.math.BigInteger;
import java.util.ArrayList;

public class SieveOfEratosthenesTest extends TestCase {
    public void testSimpleSOE() throws Exception {
        ISieveOfEratosthenes sieve = new SimpleSieveOfEratosthenes(1000000);
        sieve.maskPrimes();
        ArrayList<String> results = sieve.extractPrimes();
        for (int i = 0; i < results.size(); i++) {
            assertTrue(new BigInteger(results.get(i)).isProbablePrime(100));
        }
        assertTrue(results.size() == 78498);
    }

    public void testMultithreadSOE() throws Exception {
        ISieveOfEratosthenes sieve = new SieveOfEratosthenesMultiThread(8, 1000000);
        sieve.maskPrimes();
        ArrayList<String> results = sieve.extractPrimes();
        for (int i = 0; i < results.size(); i++) {
            assertTrue(new BigInteger(results.get(i)).isProbablePrime(100));
        }
        assertTrue(results.size() == 78498);
    }
}

