import java.awt.*;
import java.io.*;

import org.javatuples.Pair;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Plot;
import org.jfree.data.time.*;
import org.jfree.data.xy.XYDataset;


class Graphic {

    static Pair<File, JFreeChart> createGraphic(JsonWeather js, String city) {
        final TimeSeries series = new TimeSeries("Weather");
        for (Data d : js.getJs())
            series.add(new Hour(d.getDate().getHour(), d.getDate().getDay(), d.getDate().getMonth(), 2019), d.getTemperature());
        final XYDataset dataset = new TimeSeriesCollection(series);
        JFreeChart timechart = ChartFactory.createTimeSeriesChart(
                "Weather in " + city,
                "Date",
                "Temperature",
                dataset,
                false,
                false,
                false);
        Plot plot = timechart.getPlot();
        plot.setBackgroundPaint(Color.red);
        plot.setBackgroundPaint(Color.white);
        File timeChart = new File(city + ".jpeg");
        return new Pair<>(timeChart, timechart);
    }
}