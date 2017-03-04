package main;

import main.Firoozbakht.SimpleFiroozbakht;
import main.database.HSQLDBManager;

import java.util.ArrayList;

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
        System.out.println("--- Finished checking --> Start storing ---");
        ArrayList<String> res = fb.getFiroozbakhtResults();
        database.storeFiroozbakht(res);
        database.shutdown();
        System.out.println("--- Finished ---");
    }
}
