package seaFood.PTseafood.utils;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class SlugUtil {
    public static String createSlug(String str) {
        str = str.toLowerCase();
        str = str.replace("đ", "d");
        str = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        str = pattern.matcher(str).replaceAll("");
        str = str.trim().replaceAll("\\s+", "-");
        return str;
    }
}