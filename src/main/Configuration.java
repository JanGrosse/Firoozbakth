package main;

import main.database.HSQLDBManager;

public enum Configuration {
    instance;

    public static int threadCount = 8;

    public static long findPrimesUpTo = 1000000;

    public static HSQLDBManager hsqldbManager = HSQLDBManager.instance;

    public static int GUISkipNPrimes = 0;

    //0 for all
    public static int GUIMaximumPrimeGapsToShow = 0;
}