package com.example.guestlec;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;
public class Unittest {
    public static final int PASSWORD_LENGTH = 8;

    public static boolean checkEmailForValidity(String email) {

        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    public static boolean checkPasswordValidity(String password) {
        if (password.length() < PASSWORD_LENGTH)
            return false;
        else {
            int charCount = 0;
            int numCount = 0;
            int spaceCount = 0;
            int special = 0;
            int uppercase = 0;
            for (int i = 0; i < password.length(); i++) {

                char ch = password.charAt(i);

                if (Character.isDigit(ch))
                    numCount++;
                else if (Character.isLetter(ch) && Character.isLowerCase(ch))
                    charCount++;
                else if (Character.isSpace(ch))
                    spaceCount++;
                else if (Character.isUpperCase(ch))
                    uppercase++;
                else
                    special++;
            }
            int totcount = numCount + charCount + spaceCount + special;
            if (numCount >= 1 && totcount >= PASSWORD_LENGTH && spaceCount == 0 && uppercase >= 1 && charCount >= 1)
                return true;
            else
                return false;
        }
    }
}
