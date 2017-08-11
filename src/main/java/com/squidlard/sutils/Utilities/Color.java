package com.squidlard.sutils.Utilities;

import org.bukkit.ChatColor;

/**
 * Created by Dalton on 4/19/2017.
 */
public class Color {
    public static String translate(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }
}

