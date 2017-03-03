package main;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import main.database.HSQLDBManager;


public class ScatterChartSample extends Application {
    private XYChartMapper mapper = new XYChartMapper();
    private HSQLDBManager database;

    @Override
    public void start(Stage stage) {
        initAll();

        stage.setTitle("Prime Gap Function");
        final NumberAxis xAxis = new NumberAxis(1, 2, 0.05);
        final NumberAxis yAxis = new NumberAxis(0, 1000, 100);
        final ScatterChart<Number, Number> sc = new
                ScatterChart<>(xAxis, yAxis);
        xAxis.setLabel("gap size");
        yAxis.setLabel("prime size");
        sc.setTitle("Prime Gap Function");


        GridPane grid = new GridPane();
        Group root = new Group();
        Scene scene = new Scene(root, 600, 400);
        scene.setRoot(grid);
        stage.setScene(scene);
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(70);


        final Slider slider = new Slider(0, 1, 1);
        final Label sliderValue = new Label(Double.toString(slider.getValue()));
        slider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                sliderValue.setText(String.format("%.2f", new_val));
            }
        });

        GridPane.setConstraints(sliderValue, 0, 2);
        GridPane.setConstraints(slider, 0, 1);

        prepareChart(sc);

        grid.getChildren().add(sc);
        grid.getChildren().add(slider);
        grid.getChildren().add(sliderValue);

        stage.show();
    }

    private void initAll() {
        this.database = HSQLDBManager.instance;
        this.database.startup();
    }

    private void prepareChart(ScatterChart chart) {
        FiroozPrimesPair results = this.database.getFiroozbakhtPrimePairs(0, 0);
        XYChart.Series firoozbakhtXY = mapper.getXYChart(results.getFiroozs(), results.getPrimes(), "firoozbakht");
        chart.getData().addAll(firoozbakhtXY);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
