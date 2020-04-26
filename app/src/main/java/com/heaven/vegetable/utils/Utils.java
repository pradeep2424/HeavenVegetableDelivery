package com.heaven.vegetable.utils;

import android.content.Context;
import android.content.res.Resources;
import android.text.Spannable;
import android.text.style.URLSpan;
import android.util.DisplayMetrics;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static void removeUnderlines(Spannable p_Text) {
        URLSpan[] spans = p_Text.getSpans(0, p_Text.length(), URLSpan.class);

        for (URLSpan span : spans) {
            int start = p_Text.getSpanStart(span);
            int end = p_Text.getSpanEnd(span);
            p_Text.removeSpan(span);
            span = new URLSpanNoUnderline(span.getURL());
            p_Text.setSpan(span, start, end, 0);
        }
    }

    public static String convertJsonDate(String jsonDate) {
        String dateTime = "";
        try {

            String timeString = jsonDate.substring(jsonDate.indexOf("(") + 1, jsonDate.indexOf(")"));
            String[] timeSegments = timeString.split("\\+");
            // May have to handle negative timezones
//            int timeZoneOffSet = Integer.valueOf(timeSegments[1]) * 36000; // (("0100" / 100) * 3600 * 1000)
            long millis = Long.valueOf(timeSegments[0]);
            Date date = new Date(millis);

            //It Will Be in format 29-OCT-2014 2:26 PM
            dateTime = new SimpleDateFormat("dd-MMM-yyyy hh:mm aa").format(date);

//            String timeString = jsonDate.substring(jsonDate.indexOf("(") + 1, jsonDate.indexOf(")"));
//            String[] timeSegments = timeString.split("\\+");
//            // May have to handle negative timezones
//            int timeZoneOffSet = Integer.valueOf(timeSegments[1]) * 36000; // (("0100" / 100) * 3600 * 1000)
//            long millis = Long.valueOf(timeSegments[0]);
//            Date date = new Date(millis + timeZoneOffSet);
//
//            //It Will Be in format 29-OCT-2014 2:26 PM
//            dateTime = new SimpleDateFormat("dd-MMM-yyyy hh:mm aa").format(date);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dateTime;
    }

    public static int dpToPx(float dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

}
