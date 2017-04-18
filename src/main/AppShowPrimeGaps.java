package main;

import javafx.application.Application;
import javafx.stage.Stage;
import main.DataStructures.ArrayPair;
import main.database.HSQLDBManager;

public class AppShowPrimeGaps extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        HSQLDBManager database = HSQLDBManager.instance;
        database.startup();
        ArrayPair<Long, Long> primePairs = database.getPrimePrimeGapPairs(0, 0);
        new ShowPrimeGapsStage(primePairs);
    }
}
