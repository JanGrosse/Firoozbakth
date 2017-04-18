package main;

import javafx.application.Application;
import javafx.stage.Stage;
import main.Firoozbakht.FiroozbakhtMultiThread;
import main.Firoozbakht.IFiroozbakht;
import main.PrimeGapsCalculator.IPrimeGapsCalculator;
import main.PrimeGapsCalculator.PrimeGapsCalculator;
import main.SieveOfEratosthenes.ISieveOfEratosthenes;
import main.SieveOfEratosthenes.SieveOfEratosthenesMultiThread;
import main.database.HSQLDBManager;

import java.util.ArrayList;

public class MainApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        HSQLDBManager database = HSQLDBManager.instance;
        System.out.println("--- Startup database and reset ---");
        database.startup();
        database.reset();

        //SIEVE OF ERATOSTHENES
        ISieveOfEratosthenes sieveOfEratosthenes = new SieveOfEratosthenesMultiThread();
        System.out.println("--- Finished startup --> Start sieve ---");
        sieveOfEratosthenes.maskPrimes();
        System.out.println("--- Finished sieve --> Start extracting primes ---");
        ArrayList<String> primesAsStrings = sieveOfEratosthenes.extractPrimes();
        System.out.println("--- Finished extracting primes --> Start storing primes ---");
        database.storePrimes(primesAsStrings);
        //CALCULATE PRIME GAPS
        System.out.println("--- Finished storing primes --> Start calculating prime gaps ---");
        long[] primes = database.getPrimes(0, 0);
        IPrimeGapsCalculator calculator = new PrimeGapsCalculator(primes);
        calculator.calclatePrimeGaps();
        System.out.println("--- Finished calculating  prime gaps --> Start storing prime gaps ---");
        database.storePrimeGaps(calculator.getPrimeGaps());
        //CHECK FIROOZBAKHT
        System.out.println("--- Finished storing prime gaps --> Start checking Firoozbakht ---");
        IFiroozbakht firoozbakht = new FiroozbakhtMultiThread(primes);
        firoozbakht.processPrimes();
        System.out.println("--- Finished processing --> Start checking ---");
        firoozbakht.checkFalsify();
        System.out.println("--- Finished checking --> Start storing ---");
        database.storeFiroozbakht(firoozbakht.getFiroozbakhtResults());
        //START GUI
        System.out.println("--- Finished storing --> Start GUI ---");
        new ShowPrimeGapsStage(database.getPrimePrimeGapPairs());
        //ENDING
        database.shutdown();
        System.out.println("--- Finished ---");
    }
}
