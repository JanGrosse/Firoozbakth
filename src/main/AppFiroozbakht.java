package main;

import main.Firoozbakht.SimpleFiroozbakht;
import main.database.HSQLDBManager;

public class AppFiroozbakht {
    public static void main(String[] args) {
        HSQLDBManager database = HSQLDBManager.instance;
        System.out.println("--- Startup database ---");
        database.startup();
        System.out.println("--- Finished startup --> Getting primes ---");
        long[] primes = database.getPrimes(0, 0);
        System.out.println("--- Finished getting --> Start processing ---");
        SimpleFiroozbakht fb = new SimpleFiroozbakht(primes);
        fb.processPrimes();
        System.out.println("--- Finished processing --> Start checking ---");
        fb.checkFalsify();
    }
}
