import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

class JsonWeather {
    private List<Data> js;

    List<Data> getJs() {
        return js;
    }

    JsonWeather(List<String> weatherList) {
        js = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        for (String line : weatherList) {
            js.add(new Data(line, objectMapper));
        }
    }

    String getFormattedData() {
        final StringBuilder sb = new StringBuilder();
        for (Data d : js) {
            sb.append(String.format("%s   %s %s %s%s", d.getDate().getLocalDateTime(), d.getFormattedTemperature(), d.getDescription(),
                    d.getWeatherIcon(), System.lineSeparator()));
        }
        return sb.toString();
    }
}

class Data {
    private int temperature;
    private int feelsLike;
    private int minTemp;
    private int maxTemp;
    private int pressure;
    private int seaLevel;
    private int groundLevel;
    private String weatherIcon;
    private String formattedTemperature;
    private String description;
    private Date date;

    int getTemperature() {
        return temperature;
    }

    String getWeatherIcon() {
        return weatherIcon;
    }

    String getFormattedTemperature() {
        return formattedTemperature;
    }

    String getDescription() {
        return description;
    }

    Date getDate() {
        return date;
    }

    Data(String line, ObjectMapper objectMapper) {
        JsonNode mainNode;
        JsonNode weatherArrNode;
        String dateTime;
        try {
            mainNode = objectMapper.readTree(line).get("main");
            weatherArrNode = objectMapper.readTree(line).get("weather");
            for (final JsonNode objNode : weatherArrNode) {
                dateTime = objectMapper.readTree(line).get("dt_txt").toString();
                formatForecastData(dateTime, objNode.get("main").toString(), mainNode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void formatForecastData(String dateTime, String d, JsonNode mainNode) {
        temperature = mainNode.get("temp").asInt();
        feelsLike = mainNode.get("feels_like").asInt();
        minTemp = mainNode.get("temp_min").asInt();
        maxTemp = mainNode.get("temp_max").asInt();
        pressure = mainNode.get("pressure").asInt();
        seaLevel = mainNode.get("sea_level").asInt();
        groundLevel = mainNode.get("grnd_level").asInt();
        date = new Date(dateTime);
        if (temperature > 0) {
            formattedTemperature = "+" + temperature;
        } else {
            formattedTemperature = String.valueOf(temperature);
        }
        description = d.replaceAll("\"", "");
        weatherIcon = Utils.weatherIconsCodes.get(description);
    }
}


class Date {
    private int month;
    private int day;
    private String localDateTime;
    private int hour;

    private final DateTimeFormatter INPUT_DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final DateTimeFormatter OUTPUT_DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("MMM-dd HH:mm", Locale.US);

    int getMonth() {
        return month;
    }

    int getDay() {
        return day;
    }

    String getLocalDateTime() {
        return localDateTime;
    }

    int getHour() {
        return hour;
    }

    Date(String dateTime) {
        LocalDateTime parse = LocalDateTime.parse(dateTime.replaceAll("\"", ""), INPUT_DATE_TIME_FORMAT);
        localDateTime = parse.format(OUTPUT_DATE_TIME_FORMAT);
        month = getMonth(localDateTime.substring(0, 3));
        day = Integer.parseInt(localDateTime.substring(4, 6));
        hour = getTime(localDateTime.substring(7, 9));
    }

    private static int getMonth(String month) {
        Map<String, Integer> months = new HashMap<>();
        months.put("Jan", 1);
        months.put("Feb", 2);
        months.put("Mar", 3);
        months.put("Apr", 4);
        months.put("May", 5);
        months.put("Jun", 6);
        months.put("Jul", 7);
        months.put("Aug", 8);
        months.put("Sep", 9);
        months.put("Oct", 10);
        months.put("Nov", 11);
        months.put("Dec", 12);
        return months.get(month);
    }

    private static int getTime(String t) {
        char[] time = t.toCharArray();
        if (time[0] != '0')
            return Integer.parseInt(t);
        return Integer.parseInt(String.valueOf(time[1]));
    }
}