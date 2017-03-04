package main;

import main.PrimeGapsCalculator.IPrimeGapsCalculator;
import main.PrimeGapsCalculator.PrimeGapsCalculator;
import main.database.HSQLDBManager;

public class AppPrimeGaps {
    public static void main(String[] args) {
        HSQLDBManager database = HSQLDBManager.instance;
        System.out.println("--- Startup ---");
        database.startup();
        IPrimeGapsCalculator calculator = new PrimeGapsCalculator(database.getPrimes(0, 0));
        System.out.println("--- Finished startup --> Start calculating ---");
        calculator.calclatePrimeGaps();
        System.out.println("--- Finished calculating --> Start storing gaps ---");
        database.storePrimeGaps(calculator.getPrimeGaps());
        database.shutdown();
        System.out.println("--- Finished ---");
    }
}
