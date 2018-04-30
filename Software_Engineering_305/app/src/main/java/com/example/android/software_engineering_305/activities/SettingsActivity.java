package com.example.android.software_engineering_305.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.software_engineering_305.R;
import com.example.android.software_engineering_305.application.CommandInterface;
import com.example.android.software_engineering_305.application.DevDataTransfer;
import com.example.android.software_engineering_305.services.BluetoothService;
import com.example.android.software_engineering_305.services.DataLogService;
import com.example.android.software_engineering_305.services.Directories;

import java.io.File;
import java.util.Map;

/**
 * Created by Andrew on 2/19/18.
 *
 * -Implements CommandInterface to choose each command
 */

//TODO: BACKEND: Add ability to delete CSV file entry
public class SettingsActivity extends AppCompatActivity implements CommandInterface
{
    private static final String TAG = "SettingsActivity";
    private static final String FILE_NAME = "/laser.csv";
    private int[] newValues;
    private Context mContext;
    private Button  updateButton, saveButton, reloadButton;
    private SeekBar stepSpeedBar, rotationBar, pitchMinBar, ranRangeBar, lightBar;
    private Spinner cySpin, deleteSpin, loadSpinner;
    private DataLogService DLS;
    private Map<String, Integer> map;


    /**         --onCreate(...)--
     *
     * This is where the settings for the app are going to be read/changed.
     * Right now, all you can do is say hi.
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Sets up the activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mContext = this;

        map = DevDataTransfer.createHashtable();

        // Find the seek bar widgets
        stepSpeedBar = findViewById(R.id.stepSpeedBar);
        rotationBar = findViewById(R.id.rotationBar);
        pitchMinBar = findViewById(R.id.pitchMinBar);
        ranRangeBar = findViewById(R.id.ranRangeBar);
        lightBar = findViewById(R.id.lightBar);

        // Find the button widgets and give them click functionality
        updateButton = findViewById(R.id.updateBtn);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateSettings();
            }
        });
        saveButton = findViewById(R.id.saveBtn);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createSaveBox();
            }
        });
        reloadButton = findViewById(R.id.reloadBtn);
        reloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWidgets();
            }
        });

        // Find Spinner widgets
        Spinner whSpin = (Spinner) findViewById(R.id.wakeHour);
        Spinner wmSpin = (Spinner) findViewById(R.id.wakeMinute);
        Spinner wsSpin = (Spinner) findViewById(R.id.wakeSecond);
        Spinner shSpin = (Spinner) findViewById(R.id.sleepHour);
        Spinner smSpin = (Spinner) findViewById(R.id.sleepMinute);
        Spinner ssSpin = (Spinner) findViewById(R.id.sleepSecond);
        cySpin = findViewById(R.id.cycle_mode);
        loadSpinner = findViewById(R.id.load_config);
        deleteSpin = findViewById(R.id.delete_config);

        // Set string-array adapter to spinners
        ArrayAdapter<CharSequence> adapterHours = ArrayAdapter.createFromResource(this,
                R.array.hours, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapterMinSec = ArrayAdapter.createFromResource(this,
                R.array.minutesSeconds, android.R.layout.simple_spinner_item);
        final ArrayAdapter<CharSequence> adapterCycleMode = ArrayAdapter.createFromResource(this,
                R.array.cycle, android.R.layout.simple_spinner_item);

        DLS = new DataLogService(mContext);

        loadAndDelete();

        adapterHours.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterMinSec.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterCycleMode.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        whSpin.setAdapter(adapterHours);
        wmSpin.setAdapter(adapterMinSec);
        wsSpin.setAdapter(adapterMinSec);
        shSpin.setAdapter(adapterHours);
        smSpin.setAdapter(adapterMinSec);
        ssSpin.setAdapter(adapterMinSec);
        cySpin.setAdapter(adapterCycleMode);

        loadSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String itemString = loadSpinner.getSelectedItem().toString();
                String values[] = DLS.getValues(itemString);
                int[] newValues = new int[Commands.NUM_COMMANDS];
                //TODO:
                // Set each
                for(int j = 1; j < Commands.NUM_COMMANDS; j++)
                {
                    newValues[j] = Integer.parseInt(values[j+1]);
                }
                stepSpeedBar.setProgress(newValues[0]);
                pitchMinBar.setProgress(newValues[1]);
                ranRangeBar.setProgress(newValues[2]);
                cySpin.setSelection(newValues[4]);
                lightBar.setProgress(newValues[5]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        deleteSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String itemString = deleteSpin.getSelectedItem().toString();
                DLS.deleteValues(itemString);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        setWidgets();

        Log.e(TAG, "Cycle Mode: " + cySpin.getSelectedItemPosition());

        setArrayValues();
    }

    /**                 setArrayValues()
     *  The newValues array will be the values that are used to
     *  write the send commands to the Arduino. This is where they
     *  are set.
     *  Called in onCreate() and on updateSettings()
     */
    private void setArrayValues()
    {
        newValues = new int[Commands.NUM_COMMANDS];
        newValues[0] = stepSpeedBar.getProgress();
        newValues[1] = pitchMinBar.getProgress();
        newValues[2] = ranRangeBar.getProgress();
        newValues[3] = rotationBar.getProgress();
        newValues[4] = cySpin.getSelectedItemPosition();
        newValues[5] = lightBar.getProgress();
    }

    /**                 updateSettings()
     *  Updates the settings on the Arduino by taking the values from the
     *  newValues array and sending them to writeSetCommands(), where they
     *  are added to the set command string and sent to the Arduino.
     *
     */
    private void updateSettings()
    {
        Log.i(TAG, "Logging new settings...");
        setArrayValues();

        if(newValues != null)
        {
            DevDataTransfer.writeSetCommands(mContext, newValues);
            DevDataTransfer.clearValues();
        }

        Toast.makeText(mContext, "Scarecrow Updated", Toast.LENGTH_SHORT).show();
    }

    /**                     --restoreDefaults()--
     * This can set each parameter of the device's settings to the default settings we've
     * come up with. Called by settings option in the actionbar dropdown
     *
     */
    //TODO: COMMAND: Create the restoreDefaults method
    private void restoreDefaults()
    {
        // Read every line

        // Get the names

        // Make something where you can select the name

        // Get the information
    }

    /**                     --saveAsDefault()--
     * Saves the values of each widget in settings activity as a device's default settings.
     * Called by the settings option in the actionbar dropdown
     * Look into SharedPreferences to save values
     *
     */
    //TODO: COMMAND: Create the saveAsDefault method
    private void saveAsDefault(String configName)
    {
//        String data = updateButton.getText().toString() + "," + readButton.getText().toString();
//        DataLogService.log(mContext, Directories.getRootFile(mContext), data, "Send, Read");
        String[] values = new String[newValues.length + 1];
        values[0] = configName;
        if(newValues != null && newValues.length > 0)
        {
            for(int i = 0; i < newValues.length; i++)
            {
                values[i+1] = String.valueOf(newValues[i]);
            }
            DLS.writeValues(values);
            loadAndDelete();
        }
        else
            Log.e(TAG, "Value array is not instantiated.");

    }

    private void loadAndDelete()
    {
        String[] names = DLS.getNames();
        ArrayAdapter<String> adapterLoadConfig = new ArrayAdapter<>(mContext,
                android.R.layout.simple_spinner_item, names);
        ArrayAdapter<String> adapterDeleteConfig = new ArrayAdapter<>(mContext,
                android.R.layout.simple_spinner_item, names);

        adapterLoadConfig.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterDeleteConfig.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        loadSpinner.setAdapter(adapterLoadConfig);
        deleteSpin.setAdapter(adapterDeleteConfig);
    }

    private void setWidgets()
    {
        // Set values from map to seek bar widgets
        try{
            stepSpeedBar.setProgress(map.get(Commands.L_STEPPER_SPEED));
            rotationBar.setProgress(map.get(Commands.L_ROT_ANGLE));
            pitchMinBar.setProgress(map.get(Commands.L_PITCH_MIN));
            ranRangeBar.setProgress(map.get(Commands.L_PITCH_RANGE));
            lightBar.setProgress(map.get(Commands.L_LIGHT_THRES));
            // Set spinner values to correspond with read command value
            cySpin.setSelection(map.get(Commands.L_CYCLE_MODE));
        }
        catch (Exception e)
        {
            Log.e(TAG, "Error: " + e);
        }
    }

    private void createSaveBox()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Save Configuration");
        builder.setMessage("Please enter the name of the configuration that you would like to save to your Android device.");

        final EditText input = new EditText(mContext);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        builder.setView(input);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                String configName = input.getText().toString();
                if(!configName.equals(""))
                    saveAsDefault(configName);
                else
                    Toast.makeText(mContext, "Configuration Saved.", Toast.LENGTH_SHORT);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });
        builder.show();
    }

    /**                     --onBackPressed()--
     *  Returns to the parent activity (ScanActivity) if the back button is pressed
     *  Also, disconnects from the current device
     */
    @Override
    public void onBackPressed()
    {
        DevDataTransfer.clearValues();
        BluetoothService.disconnect(mContext);
        Log.e(TAG, "Back pressed. Navigating to " + getParentActivityIntent());
        Intent intent = this.getParentActivityIntent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        super.onBackPressed();
    }

    /**                     --onDestroy()--
     *  If the app closes at this point, disconnect from Bluetooth device first
     */
    @Override
    protected void onDestroy()
    {
        BluetoothService.disconnect(mContext);
        super.onDestroy();
    }

    /**               --onOptionsItemSelected(...)--
     *
     * Same functionality as pressing the back button. This will open the parent activity (ScanActivity)
     * Also, disconnects from the current device
     *
     * @param item: Item from the action bar that was selected, in this case the back button
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                Log.e(TAG, "Home item pressed. Navigating to " + getParentActivityIntent());
                BluetoothService.disconnect(mContext);
                Intent intent = this.getParentActivityIntent();
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
        }
        return true;
    }
}
