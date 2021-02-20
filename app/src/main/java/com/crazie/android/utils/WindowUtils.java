package com.crazie.android.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class WindowUtils {

    private static DisplayMetrics displayMetrics;

    public static int getHeight(Context context) {
        setupDisplayMetrics(context);
        return displayMetrics.heightPixels;
    }

    private static void setupDisplayMetrics(Context context) {
        if (displayMetrics == null)
            displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getMetrics(displayMetrics);
    }

}
