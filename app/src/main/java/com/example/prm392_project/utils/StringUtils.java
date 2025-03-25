package com.example.prm392_project.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    public static String decodeUnicode(String unicodeStr) {
        Pattern pattern = Pattern.compile("\\\\u([0-9A-Fa-f]{4})");
        Matcher matcher = pattern.matcher(unicodeStr);
        StringBuffer decodedStr = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(decodedStr, Character.toString((char) Integer.parseInt(matcher.group(1), 16)));
        }
        matcher.appendTail(decodedStr);
        return decodedStr.toString();
    }

    public static String moneySplitter(String money) {
        String[] parts = money.split("\\.");
        String part1 = parts[0];
        String part2 = parts[1];
        String result = "";
        int count = 0;
        for (int i = part1.length() - 1; i >= 0; i--) {
            count++;
            result = part1.charAt(i) + result;
            if (count % 3 == 0 && i != 0) {
                result = "," + result;
            }
        }
        return result + "." + part2;
    }
}
