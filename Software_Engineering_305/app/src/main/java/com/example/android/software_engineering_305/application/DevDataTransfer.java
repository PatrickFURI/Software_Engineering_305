package com.example.android.software_engineering_305.application;

import android.content.Context;
import android.util.Log;

import com.example.android.software_engineering_305.services.BluetoothService;

import java.util.HashMap;
import java.util.Map;
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
public class DevDataTransfer implements CommandInterface
{
    private static final String TAG = "DevDataTransfer";
    // Andrew: You'll be sending these two strings after you collect all the data
    private static String  values = "",
                    codes = "";

    /**                 --parseResponse(...)--
     * Takes the string from the BluetoothService read() and creates two strings
     * that can be parsed and made into a hashtable
     *
     * @param response
     */
    public static void parseResponse(String response)
    {
        // Preprocesses the string
        String[] result = response.split("\\r?\\n");
        for(String line : result)
        {
            // Seperates the string into two different ones
            if(!line.equals("ok"))
            {
                Pattern pattern = Pattern.compile("  *");
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    codes += line.substring(0, matcher.start()) + ' ';
                    values += line.substring(matcher.end()) + ' ';
                }
            }
        }
        Log.e(TAG, "Codes: " + codes);
        Log.e(TAG, "Values: " + values);
    }

    /**
     *
     * @return: Returns the map to the SettingsActivity
     */
    // TODO (Jack): Value will be set to "error(6)" if it was not received. Check for that here (or somewhere else!)
    public static Map<String, String> createHashtable() {
        Map<String, String> map = new HashMap<String, String>();
        String[] keys = codes.split(" ");
        String[] valArray = values.split(" ");
        for (int i = 0 ; i < keys.length ; i++) {
            try {
                map.put(keys[i], valArray[i]);
            }
            catch (Exception e) {
                System.out.println("exception caused by " + e);
            }
        }
        return map;
    }

    /**
     *              --writeLookCommands()--
     *      Writes the look commands to the Arduino
     * @param mContext
     */
    public static void writeLookCommands(Context mContext)
    {
        for(int i = 0; i < Commands.NUM_COMMANDS; i++)
        {
            switch(i)
            {
                case 0:
                    BluetoothService.write(mContext, "L101");
                    Log.e(TAG, "Wrote and read for L101");
                    break;
                case 1:
                    BluetoothService.write(mContext, "L131");
                    Log.e(TAG, "Wrote and read for L131");
                    break;
                case 2:
                    BluetoothService.write(mContext, "L132");
                    Log.e(TAG, "Wrote and read for L132");
                    break;
                case 3:
                    BluetoothService.write(mContext, "L111");
                    Log.e(TAG, "Wrote and read for L131");
                    break;
                case 4:
                    BluetoothService.write(mContext, Commands.CYCLE_MODE);
                    Log.e(TAG, "Wrote Cycle Mode");
                    break;
                case 5:
                    BluetoothService.write(mContext, "L221");
                    Log.e(TAG, "Wrote and read for L221");
                    break;
                case 6:
                    BluetoothService.write(mContext, "L251");
                    Log.e(TAG, "Wrote and read for L251");
                    break;
                case 7:
                    BluetoothService.write(mContext, "L252");
                    Log.e(TAG, "Wrote and read for L252");
                    break;
                case 8:
                    BluetoothService.write(mContext, "L261");
                    Log.e(TAG, "Wrote and read for L261");
                    break;
                case 9:
                    BluetoothService.write(mContext, "L262");
                    Log.e(TAG, "Wrote and read for L262");
                    break;
            }
        }
    }
}
