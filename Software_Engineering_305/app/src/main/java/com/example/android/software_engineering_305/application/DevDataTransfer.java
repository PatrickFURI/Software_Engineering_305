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
    private static String  values = "",
                    codes = "";

    public static void parseResponse(String response)
    {
        String[] result = response.split("\\r?\\n");
        for(String line : result)
        {
            if(!line.equals("ok"))
            {
                Log.e(TAG, "Line: " + line);
                Pattern pattern = Pattern.compile("  *");
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    codes += line.substring(0, matcher.start()) + ' ';
                    values += line.substring(matcher.end()) + ' ';
                }
            }
        }

        Log.e(TAG, "Values: " + values);
        Log.e(TAG, "Codes: " + codes);
    }

    // Jack: Map the values and codes into two arrays

    // Send the values in a hashtable with key of code and value of values to the settings activity
}
