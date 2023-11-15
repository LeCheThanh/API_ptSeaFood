package seaFood.PTseafood.utils;

import java.util.Random;

public class GenerateCodeUtil {
    public static String GenerateCodeOrder(){
        long timestamp = System.currentTimeMillis();

        int codeLength = 6; // Độ dài của phần mã ngẫu nhiên
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder randomString = new StringBuilder();

        Random random = new Random();
        for (int i = 0; i < codeLength; i++) {
            int index = random.nextInt(characters.length());
            char randomChar = characters.charAt(index);
            randomString.append(randomChar);
        }
        return timestamp + randomString.toString();
    }
}
