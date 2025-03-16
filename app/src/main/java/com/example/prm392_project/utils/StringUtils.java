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
}
