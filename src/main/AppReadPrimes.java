package main;

import main.database.HSQLDBManager;

public class AppReadPrimes {
    public static void main(String[] args) {
        HSQLDBManager database = HSQLDBManager.instance;
        database.startup();
        ArrayPair<Long, Long> results = database.getPrimePrimeGapPairs(1000000,0);
        Long[] primes = results.getFirstArray();
        Long[] firooz = results.getSecondArray();
        System.out.println("Primes:" + primes.length + "\nGapsize: " + firooz.length);
        for (int i = 0; i < primes.length; i++) {
            System.out.println(primes[i] + " to " + firooz[i]);
        }
        database.shutdown();
    }
}
