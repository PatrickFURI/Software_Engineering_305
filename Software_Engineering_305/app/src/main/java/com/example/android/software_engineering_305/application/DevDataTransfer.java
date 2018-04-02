package com.example.android.software_engineering_305.application;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Created by Jack and Andrew
 *
 * DevDataTransfer
 *
 * Acts as a holding area for the values gathered from the Bluetooth device before
 * the values are sent to the SettingsActivity
 *
 * Called when settings button in ScanActivity is clicked / when SettingsActivity is created
 *
 */
public class DevDataTransfer
{
    private static final String TAG = "DevDataTransfer";
    // Andrew: You'll be sending these two strings after you collect all the data
    private String values, codes;

    public static void parseResponse(String response)
    {
        Pattern pattern = Pattern.compile(", *");
        Matcher matcher = pattern.matcher(response);
        if (matcher.find()) {
            String string1 = response.substring(0, matcher.start());
            String string2 = response.substring(matcher.end());

            Log.e(TAG, "String 1: " + string1 + "  String 2: " + string2);
        }

    }

    // Jack: Map the values and codes into two arrays

    // Send the values in a hashtable with key of code and value of values to the settings activity
}
