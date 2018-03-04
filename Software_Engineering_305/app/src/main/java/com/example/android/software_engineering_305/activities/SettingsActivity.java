package com.example.android.software_engineering_305.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.android.software_engineering_305.R;
import com.example.android.software_engineering_305.services.BluetoothService;

/**
 * Created by Andrew on 2/19/18.
 */

public class SettingsActivity extends AppCompatActivity
{
    private static final String TAG = "SettingsActivity";
    private Context mContext;
    private Button sendButton;

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

        sendButton = findViewById(R.id.write_btn);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BluetoothService.write(mContext, "X0");
                //BluetoothService.read(mContext);
            }
        });
    }

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
        switch (item.getItemId()) {
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
