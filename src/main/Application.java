package main;

public class Application {
    public static void main(String... args) {
        SieveOfEratosthenes soe = new SieveOfEratosthenes(99999);
        soe.calculatePrimes();
        System.out.println(soe.extractPrimes());
        System.out.println("Finished!");
    }
}