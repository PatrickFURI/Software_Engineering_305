package com.example.android.software_engineering_305.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

import com.example.android.software_engineering_305.R;
import com.example.android.software_engineering_305.application.CommandInterface;
import com.example.android.software_engineering_305.services.BluetoothService;
import com.example.android.software_engineering_305.services.DataLogService;
import com.example.android.software_engineering_305.services.Directories;

/**
 * Created by Andrew on 2/19/18.
 *
 * -Implements CommandInterface to choose each command
 */

//TODO: BACKEND: Send the information from the device to this activity via a handler
public class SettingsActivity extends AppCompatActivity implements CommandInterface
{
    private static final String TAG = "SettingsActivity";
    private Context mContext;
    private Button sendButton;
    private Button readButton;

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

        //TODO: UI: Create the widgets used to store and change device information
        /*
        -Slider for top, middle, and bottom knob
        -Other widget for Cycle Mode
        -Other widget for Start / Stop time
        -Other widget for light threshold
         */

        sendButton = findViewById(R.id.write_btn);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BluetoothService.write(mContext, Commands.DEBUG_AVAILABLE);
                //BluetoothService.read(mContext);
            }
        });

        readButton = findViewById(R.id.read_button);
        readButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BluetoothService.read(mContext);
            }
        });
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
        String data = sendButton.getText().toString() + "," + readButton.getText().toString();
        DataLogService.log(mContext, Directories.getRootFile(mContext), data, "Send, Read");
    }

    //TODO: UI: Create a settings menu in the action bar to call default methods. Going to need menu layout file
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu)
//    {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.main_menu, menu);
//        return true;
//    }


    /**                     --onBackPressed()--
     *  Returns to the parent activity (ScanActivity) if the back button is pressed
     *  Also, disconnects from the current device
     */
    @Override
    public void onBackPressed()
    {
        BluetoothService.disconnect(mContext);
        Log.e(TAG, "Back pressed. Navigating to " + getParentActivityIntent());
        Intent intent = this.getParentActivityIntent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        super.onBackPressed();
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

            //TODO: UI: Add functionality to Settings Menu
        }
        return true;
    }
}
