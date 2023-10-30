package seaFood.PTseafood.utils;

import java.util.regex.Pattern;

public class PhoneNumberValidator {
    public static boolean validateVNPhoneNumber(String phoneNumber) {
        Pattern pattern = Pattern.compile("^(0)(91|94|88|83|84|85|81|82|86|96|97|98|32|33|34|35|36|37|38|39|89|90|93|70|79|77|76|78)\\d{7}$");
        return pattern.matcher(phoneNumber).matches();
    }
}
