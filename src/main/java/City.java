import java.util.*;

class DateAndWeather {
    int day;
    int hour;
    int temperature;
    int year;
    int month;

    public DateAndWeather(int day, int hour, int temperature, int year, int month) {
        this.day = day;
        this.hour = hour;
        this.temperature = temperature;
        this.year = year;
        this.month = month;
    }

}

class City {
    public static List<DateAndWeather> getData(String w) {
        List<DateAndWeather> result = new ArrayList<DateAndWeather>() {
        };
        String[] weather = w.split("\n");
        for (String s : weather) {
            int month = getMonth(s.substring(0, 3));
            int day = Integer.parseInt(s.substring(4, 6));
            int hour = getTime(s.substring(7, 9));
            int temperature = getTemperature(s.substring(15, 18));
            DateAndWeather dw = new DateAndWeather(day, hour, temperature, 2019, month);
            result.add(dw);
        }
        return result;
    }

    private static int getMonth(String month) {
        Map<String, Integer> months = new HashMap<String, Integer>();
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

    private static int getTemperature(String t) {
        char[] temperature = t.toCharArray();
        if (temperature[0] != '-' && temperature[1] == ' ')
            return Integer.parseInt(String.valueOf(temperature[0]));
        if (temperature[0] != '-' && temperature[1] != ' ')
            return Integer.parseInt(t.substring(0, 2));
        if (temperature[0] == '-' && temperature[2] == ' ')
            return Integer.parseInt(t.substring(0, 2));
        return Integer.parseInt(t);
    }
}