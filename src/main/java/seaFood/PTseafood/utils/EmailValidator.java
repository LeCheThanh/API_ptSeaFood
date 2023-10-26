package seaFood.PTseafood.utils;

import java.util.regex.Pattern;

public class EmailValidator {
    public static boolean validateEmail(String email) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
        return pattern.matcher(email).matches();
    }
}
