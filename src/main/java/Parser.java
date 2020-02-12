import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.javatuples.Pair;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


class Parser {
    private static final String API_CALL_TEMPLATE = "https://api.openweathermap.org/data/2.5/forecast?q=";
    private static final String USER_AGENT = "Mozilla/5.0";
    private final static Cache<String, Pair<String, Pair<File, JFreeChart>>> cache = Caffeine.newBuilder().
            expireAfterWrite(1, TimeUnit.HOURS).
            maximumWeight(100).
            weigher((k,v)->1).
            build();

    private static String downloadJsonRawData(String city) throws Exception {
        String api_key = FileWork.readApiKey();
        String urlString = API_CALL_TEMPLATE + city + api_key;
        URL urlObject = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = connection.getResponseCode();
        if (responseCode == 404) {
            throw new IllegalArgumentException();
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }

    private static List<String> makeWeatherList(String data) throws Exception {
        List<String> weatherList = new ArrayList<>();

        JsonNode arrNode = new ObjectMapper().readTree(data).get("list");
        if (arrNode.isArray()) {
            for (final JsonNode objNode : arrNode)
                weatherList.add(objNode.toString());
        }
        return weatherList;
    }

    static String getWeather(String city) throws Exception {
        Pair<String, Pair<File, JFreeChart>> jsBuff = cache.getIfPresent(city);
        if (jsBuff != null) {
            ChartUtilities.saveChartAsJPEG(jsBuff.getValue1().getValue0(), jsBuff.getValue1().getValue1(), 720, 480);
            return jsBuff.getValue0();
        }
        String data = downloadJsonRawData(city);
        List<String> convertData = makeWeatherList(data);
        JsonWeather js = new JsonWeather(convertData);
        String weatherList = js.getFormattedData();
        Pair<File, JFreeChart> graphic = Graphic.createGraphic(js, city);
        ChartUtilities.saveChartAsJPEG(graphic.getValue0(), graphic.getValue1(), 720, 480);
        cache.put(city, new Pair<>(weatherList, graphic));
        return weatherList;
    }
}