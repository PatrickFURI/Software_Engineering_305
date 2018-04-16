package com.example.android.software_engineering_305.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.example.android.software_engineering_305.R;
import com.example.android.software_engineering_305.application.CommandInterface;
import com.example.android.software_engineering_305.application.DevDataTransfer;
import com.example.android.software_engineering_305.services.BluetoothService;
import com.example.android.software_engineering_305.services.DataLogService;
import com.example.android.software_engineering_305.services.Directories;

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
    private Context mContext;
    private Button  updateButton, readButton;
    private SeekBar stepSpeedBar, rotationBar, pitchMinBar, ranRangeBar, lightBar;

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

        // Find the seek bar widgets
        stepSpeedBar = findViewById(R.id.stepSpeedBar);
        rotationBar = findViewById(R.id.rotationBar);
        pitchMinBar = findViewById(R.id.pitchMinBar);
        ranRangeBar = findViewById(R.id.ranRangeBar);
        lightBar = findViewById(R.id.lightBar);

        //TODO: This is where you get the map
        Map<String, String> map = DevDataTransfer.createHashtable();

        // Find the button widgets and give them click functionality
        updateButton = findViewById(R.id.updateBtn);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // BluetoothService.write(mContext, Commands.DEBUG_AVAILABLE);
                updateSettings();
            }
        });
    }

    private void updateSettings()
    {
        //TODO: Get values from the SeekBar and add them to set command
        Log.i(TAG, "Logging new settings...");
        int[] newValues = new int[Commands.NUM_COMMANDS];
        newValues[0] = stepSpeedBar.getProgress();
        newValues[1] = pitchMinBar.getProgress();
        newValues[2] = ranRangeBar.getProgress();
        newValues[3] = rotationBar.getProgress();
        newValues[4] = 1; //Cycle Mode
        newValues[5] = lightBar.getProgress();

        DevDataTransfer.writeSetCommands(mContext, newValues);
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
    private void saveAsDefault()
    {
        String data = updateButton.getText().toString() + "," + readButton.getText().toString();
        DataLogService.log(mContext, Directories.getRootFile(mContext), data, "Send, Read");
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
