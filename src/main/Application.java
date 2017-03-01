package main;

import main.SieveOfEratosthenes.SimpleSieveOfEratosthenes;
import main.database.HSQLDBManager;

public class Application {
    public static void main(String... args) {
        HSQLDBManager database = HSQLDBManager.instance;
        //database.startup();i
        //database.reset();
        SimpleSieveOfEratosthenes soe = new SimpleSieveOfEratosthenes(1000063);
        System.out.println("--- Finished Startup --> Start sieve ---");
        soe.maskPrimes();
        System.out.println("--- Finished sieve --> Start extracting primes ---");
        System.out.println(soe.getBitSet().toString());
        //ArrayList<String> primes = soe.extractPrimes();
        //System.out.println("--- Finished extracting primes --> Start storing primes ---");
       // database.storePrimes(primes);
       // database.outputPrimes(0);
        //database.shutdown();
        System.out.println("Finished!");
    }
}

