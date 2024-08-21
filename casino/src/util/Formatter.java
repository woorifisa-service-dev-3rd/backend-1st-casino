package util;

import java.text.NumberFormat;
import java.util.Locale;

public class Formatter {
    public static String formatToCurrency(int amount) {
        NumberFormat formatter = NumberFormat.getInstance(Locale.KOREA);
        return formatter.format(amount) + "원";
    }
}
