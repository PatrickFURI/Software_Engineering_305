package com.example.android.software_engineering_305.services;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Matt Constant on 2/22/17.
 *
 * Data Log Service is responsible for saving data to a file. For now, all data is saved in CSV
 * format and the contents must be passed in the proper format. In order to use this service, call
 * the static log(Context, File, String, String) method provided by the Service. This ensures the
 * Service is called with the proper information given to it.
 */

public class DataLogService extends Service{
    /**
     * File to save the contents in. This Service creates all directories and files as needed
     * to save the data.
     */
    private static final String EXTRA_FILE_DESTINATION = "uri.wbl.ear.extra_file_destination";
    /**
     * The data to be saved in the given file.
     */
    private static final String EXTRA_DATA = "uri.wbl.ear.extra_data";
    /**
     * The header for the file. This is used for CSV formats as the first row of values. If the
     * file passed in EXTRA_FILE_DESTINATION does not exist, this header is applied before the
     * data that is passed. Otherwise, the header is not used.
     */
    private static final String EXTRA_HEADER = "uri.wbl.ear.extra_header";

    private static File FILE_PATH;

    /**
     * The log(Context, File, String, String) method allows for other components to use this
     * Service.
     * @param context The Caller's instance. Needed to pass the Intent to this Service.
     * @param file The File the data should be saved to.
     * @param data The data to be saved in the file.
     * @param header The header that should be applied to an empty CSV file.
     */
    public static void log(Context context, File file, String data, String header) {
        Intent intent = new Intent(context, DataLogService.class);
        intent.putExtra(EXTRA_FILE_DESTINATION, file.getAbsolutePath());
        intent.putExtra(EXTRA_DATA, data);
        intent.putExtra(EXTRA_HEADER, header);
        logIntent(intent);
    }


    /**
     * The onHandleIntent(Intent) method is called every time the Service receives a new Intent.
     * Every Intent passed to this Service represents a new set of data to be saved to a file.
     * @param intent Contains the File path, Data, and Header.
     */
    public static void logIntent(Intent intent) {
        if (intent == null || !intent.hasExtra(EXTRA_DATA)) {
            return;
        }

        // if this file is new, create the file and flag as new.
        boolean newFile = false;
        File file = new File(intent.getStringExtra(EXTRA_FILE_DESTINATION));
        if (!file.exists()) {
            newFile = true;
            file.getParentFile().mkdirs();
            try {
                if (!file.createNewFile()) {
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FILE_PATH = file;

        // write to the file
        try
        {
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            if (newFile)
            {
                // if flagged as a new file, apply the header before the data.
                fileOutputStream.write(intent.getStringExtra(EXTRA_HEADER).getBytes());
                fileOutputStream.write("\n".getBytes());
            }
            fileOutputStream.write(intent.getStringExtra(EXTRA_DATA).getBytes());
            fileOutputStream.write("\n".getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
            System.out.println("File creation completed.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static File getFilePath() {
        return FILE_PATH;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
