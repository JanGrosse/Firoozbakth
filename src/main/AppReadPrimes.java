package main;

import javafx.util.Pair;
import main.database.HSQLDBManager;

public class AppReadPrimes {
    public static void main(String[] args) {
        HSQLDBManager database = HSQLDBManager.instance;
        database.startup();
        FiroozPrimesPair results = database.getFiroozbakhtPrimePairs(0,0);
        long[] primes = results.getPrimes();
        double[] firooz = results.getFiroozs();
        System.out.println("Primes:" + primes.length + "\nFiroozbakhts: " + firooz.length);
        for (int i = 0; i < primes.length; i++) {
            System.out.println(primes[i] + " to " + firooz[i]);
        }
    }
}
