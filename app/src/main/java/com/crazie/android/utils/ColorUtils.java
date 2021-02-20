package com.crazie.android.utils;

import android.graphics.Color;

public class ColorUtils {

    public static int parse(String color) {

        switch (color.length()) {
            case 4:
                color = color.replaceAll("#([0-9a-fA-F])([0-9a-fA-F])([0-9a-fA-F])", "#$1$1$2$2$3$3");
            case 5:
                color = color.replaceAll("#([0-9a-fA-F])([0-9a-fA-F])([0-9a-fA-F])([0-9a-fA-F])", "#$2$2$3$3$4$4");
        }

        return Color.parseColor(color);
    }


}
