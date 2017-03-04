package main;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import main.database.HSQLDBManager;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;


public class AppShowPrimeGaps extends Application {
    private HSQLDBManager database;
    private int customXMaximum = 10000;
    private int definedYMaximum = 45;
    private ArrayList<Long> allPrimes;
    private ArrayList<Long> allPrimeGaps;
    private NumberAxis yAxis;
    private NumberAxis xAxis;

    @Override
    public void start(Stage stage) {
        Group root = new Group();
        Scene scene = new Scene(root, 820, 700);
        stage.setTitle("Loading... This may take some time...");
        stage.setScene(scene);
        stage.show();
        initAll();

        yAxis = new NumberAxis(0, definedYMaximum, 1);
        yAxis.setLabel("gap size");
        xAxis = new NumberAxis(0, customXMaximum, 1000);
        xAxis.setLabel("primes");

        final ScatterChart<Number, Number> chart = new ScatterChart<>(xAxis, yAxis);
        chart.setTitle("Prime Gap Function");
        chart.setMinHeight(600);
        chart.setMinWidth(800);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(70);
        scene.setRoot(grid);

        final Slider slider = new Slider(10000, 1000000, customXMaximum);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(100000);
        slider.setMinorTickCount(9);
        slider.setSnapToTicks(true);
        final Label sliderLabel = new Label(Integer.toString((int) slider.getValue()));
        slider.valueProperty().addListener((ov, old_val, new_val) -> {
            sliderLabel.setText(new DecimalFormat("0").format(new_val));
            recalculateBounds(Double.valueOf(new_val.toString()).intValue());
        });
        sliderLabel.setMinWidth(800);
        sliderLabel.setAlignment(Pos.CENTER);


        GridPane.setConstraints(sliderLabel, 0, 2);
        GridPane.setConstraints(slider, 0, 1);

        setDatapoints(chart);

        grid.getChildren().add(chart);
        grid.getChildren().add(slider);
        grid.getChildren().add(sliderLabel);

        stage.setTitle("Prime Gap Function");
        scene.getStylesheets().add("main/chart.css");
        stage.setScene(scene);
        stage.show();
    }

    private void recalculateBounds(int new_val) {
        this.customXMaximum = new_val;
        this.xAxis.setUpperBound(this.customXMaximum);
        long highest = 0;
        int size = allPrimes.size(), index = 0, highestIndex = 0;
        while (index < size && allPrimes.get(index) < customXMaximum) index++;
        index--;
        while (index != 0) {
            if (allPrimeGaps.get(index) > highest) {
                highest = allPrimeGaps.get(index);
                highestIndex = index;
            }
            index--;
        }
        this.definedYMaximum = allPrimeGaps.get(highestIndex).intValue();
        this.yAxis.setUpperBound(this.definedYMaximum + 10);
    }

    private void initAll() {
        this.database = HSQLDBManager.instance;
        this.database.startup();
        ArrayPair<Long, Long> data = database.getPrimePrimeGapPairs(0, 0);
        this.allPrimes = new ArrayList(Arrays.asList(data.getFirstArray()));
        this.allPrimeGaps = new ArrayList(Arrays.asList(data.getSecondArray()));
    }

    private void setDatapoints(XYChart chart) {
        XYChart.Series firoozbakhtXY = createDataSeries(allPrimes, allPrimeGaps, "prime gaps");
        chart.getData().addAll(firoozbakhtXY);
    }

    public XYChart.Series createDataSeries(ArrayList<Long> dataX, ArrayList<Long> dataY, String name) {
        XYChart.Series<Long, Long> series = new XYChart.Series<>();
        XYChart.Data<Long, Long>[] datas = new XYChart.Data[dataX.size()];
        series.setName(name);
        for (int i = 0; i < dataX.size(); i++) {
            datas[i] = new XYChart.Data(dataX.get(i), dataY.get(i));
        }
        series.getData().addAll(datas);
        return series;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
