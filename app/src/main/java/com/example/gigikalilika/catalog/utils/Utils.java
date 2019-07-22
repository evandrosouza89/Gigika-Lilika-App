package com.example.gigikalilika.catalog.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

import java.util.IllegalFormatException;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Utils {

    public static String formatPrice(Double price) {
        String formattedPrice = "Indisponível";
        try {
            formattedPrice = "R$: " + String.format("%.2f", price);
        } catch (IllegalFormatException e) {
            return formattedPrice;
        }
        return formattedPrice;
    }

    public static int dpToPixel(Context context, int dp) {
        Resources r = context.getResources();

        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.getDisplayMetrics()
        );
    }
}