package main;

import main.SieveOfEratosthenes.SieveOfEratosthenesMultiThread;
import main.database.HSQLDBManager;

import java.util.ArrayList;

public class AppMultithreadSoE {
    public static void main(String... args) {
        HSQLDBManager database = HSQLDBManager.instance;
        System.out.println("--- Startup database ---");
        database.startup();
        database.reset();
        SieveOfEratosthenesMultiThread mediator = new SieveOfEratosthenesMultiThread(8, 1000);
        System.out.println("--- Finished startup --> Start sieve ---");
        mediator.maskPrimes();
        System.out.println("--- Finished sieve --> Start extracting primes ---");
        ArrayList<String> primes = mediator.extractPrimes();
        System.out.println("--- Finished extracting primes --> Start storing primes ---");
        database.storePrimes(primes);
        database.shutdown();
        System.out.println("Finished!");
    }
}

