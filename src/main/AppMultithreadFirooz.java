package main;

import main.Firoozbakht.FiroozbakhtMultithreading;
import main.database.HSQLDBManager;

public class AppMultithreadFirooz {
    public static void main(String[] args) {
        HSQLDBManager database = HSQLDBManager.instance;
        System.out.println("--- Startup database ---");
        database.startup();
        System.out.println("--- Finished startup --> Getting primes ---");
        long[] primes = database.getPrimes(0, 0);
        System.out.println("--- Finished getting --> Start processing ---");
        System.out.println(primes.length + " Primes were loaded! last prime: " + primes[primes.length - 1]);
        FiroozbakhtMultithreading fb = new FiroozbakhtMultithreading(primes, 8);
        fb.processPrimes();
        System.out.println("--- Finished processing --> Start checking ---");
        fb.checkFalsify();
        System.out.println("--- Finished checking --> Start storing ---");
        database.storeFiroozbakht(fb.getFiroozbakhtResults());
        database.shutdown();
        System.out.println("--- Finished ---");
    }
}
