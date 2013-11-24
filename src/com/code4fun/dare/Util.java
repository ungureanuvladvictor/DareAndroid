package com.code4fun.dare;

import android.content.Context;
import android.widget.Toast;

public class Util {
    public static void inform(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
