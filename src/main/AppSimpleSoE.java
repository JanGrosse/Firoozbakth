package main;

import main.SieveOfEratosthenes.SimpleSieveOfEratosthenes;
import main.database.HSQLDBManager;

import java.util.ArrayList;

public class AppSimpleSoE {
    public static void main(String... args) {
        HSQLDBManager database = HSQLDBManager.instance;
        System.out.println("--- Startup database ---");
        database.startup();
        database.reset();
        SimpleSieveOfEratosthenes soe = new SimpleSieveOfEratosthenes(10000000);
        System.out.println("--- Finished startup --> Start sieve ---");
        soe.maskPrimes();
        System.out.println("--- Finished sieve --> Start extracting primes ---");
        ArrayList<String> primes = soe.extractPrimes();
        System.out.println("--- Finished extracting primes --> Start storing primes ---");
        database.storePrimes(primes);
        database.shutdown();
        System.out.println("Finished!");
    }
}

