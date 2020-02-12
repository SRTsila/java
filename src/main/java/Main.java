import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import java.util.List;

public class Main {
    public static void main(String args[]) throws Exception {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new Bot());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }

//        List<DateAndWeather> dw = City.getData("Dec-15 18:00   -11 Clouds ☁\n" +
//                "Dec-15 21:00   -10 Clouds ☁\n" +
//                "Dec-16 00:00   -8 Clouds ☁\n" +
//                "Dec-16 03:00   -8 Clouds ☁\n" +
//                "Dec-16 06:00   -7 Clouds ☁\n" +
//                "Dec-16 09:00   -5 Clouds ☁\n" +
//                "Dec-16 12:00   -7 Clouds ☁\n" +
//                "Dec-16 15:00   -7 Clouds ☁\n" +
//                "Dec-16 18:00   -6 Clouds ☁\n" +
//                "Dec-16 21:00   -6 Clouds ☁\n" +
//                "Dec-17 00:00   -6 Clouds ☁\n" +
//                "Dec-17 03:00   -6 Clouds ☁\n" +
//                "Dec-17 06:00   -5 Clouds ☁\n" +
//                "Dec-17 09:00   -4 Clouds ☁\n" +
//                "Dec-17 12:00   -5 Clouds ☁");
//        Graphic.createGraphic(dw, "Yekaterinburg");
    }
}
