package me.downn_falls.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static String colorize(String s) {
        if (s == null || s.isEmpty())
            return "";
        Pattern pattern = Pattern.compile("&#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(s);
        while (matcher.find()) {
            String hexColor = s.substring(matcher.start(), matcher.end());
            s = s.replace(hexColor, net.md_5.bungee.api.ChatColor.of(hexColor.substring(1)).toString());
            matcher = pattern.matcher(s);
        }

        return net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', s);
    }

}
