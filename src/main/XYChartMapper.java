package main;


import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;

public class XYChartMapper {
    public Series getXYChart(double[] dataX, long[] dataY, String name) {
        Series series = new XYChart.Series();
        series.setName(name);
        for (int i = 0; i < dataX.length; i++) {
            series.getData().add(new XYChart.Data(dataX[i], dataY[i]));
        }
        return series;
    }
}
