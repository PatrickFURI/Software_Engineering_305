package com.example.android.software_engineering_305.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.software_engineering_305.R;

/**                 -- MainActivity --
 *
 *  This is the activity (screen) that the app first loads.
 *  Its main function is to ask for the permissions that the app requires to run
 *
 */
public class MainActivity extends AppCompatActivity
{
    private static final String TAG = "MainActivity";
    private final static String[] PERMISSIONS = {
            Manifest.permission.BLUETOOTH,                  // Required for BLE connection, read, write
            Manifest.permission.BLUETOOTH_ADMIN,            // Required to access paired Bluetooth devices
            Manifest.permission.ACCESS_COARSE_LOCATION,      // Required to search for Bluetooth devices
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int PERMISSIONS_CODE = 111;
    private boolean enabledBT, enabledBTA, enabledCL;
    private Context mContext;
    private Button  startButton;
    private TextView placeHolderText;

    /**         --onCreate(...)--
     *   Executes when the MainActivity is launched (when the app opens).
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Sets up the activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        // Start logo animation
        placeHolderText = findViewById(R.id.placeholderLogo);
        Animation fadeInAnimation = AnimationUtils.loadAnimation(mContext, R.anim.logofadein);
        placeHolderText.startAnimation(fadeInAnimation);

        // Sets up the start button
        startButton = findViewById(R.id.main_start);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                // Leaves this activity, goes to ScanActivity
                Intent intent = new Intent(mContext, ScanActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        // Prints to the console that the activity finished loading
        Log.i(TAG, "MainActivity loaded.");
    }

    /**         --askPermissions()--
     * Required for newer Android devices
     *
     * We ask the user for their permission to use Bluetooth functionality on their device.
     * If they do not give us permission, they cannot use this application, so access to the next
     * page requires all permissions to be granted.
     *
     */
    private void askPermissions() {
        // Check Permissions at Runtime (Android M+), and Request if Necessary
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // The Results from this Request are handled in a Callback Below.
            requestPermissions(PERMISSIONS, PERMISSIONS_CODE);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        // Asks for permissions when the application starts
        askPermissions();
    }
}
