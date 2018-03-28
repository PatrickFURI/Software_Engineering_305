package com.example.android.software_engineering_305.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
    private static final int MY_PERMISSIONS_REQUEST_BLUETOOTH = 110;
    private static final int MY_PERMISSIONS_REQUEST_BLUETOOTH_ADMIN = 111;
    private static final int MY_PERMISSIONS_REQUEST_COARSE_LOC = 112;
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

        // Asks for the permissions
        askPermissions();

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
                // Checks to see if permissions are granted
                if(enabledBT && enabledBTA && enabledCL)
                {
                    // Leaves this activity, goes to ScanActivity
                    Intent intent = new Intent(mContext, ScanActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    // Asks for the permissions
                    Toast.makeText(mContext, "Please enable all permissions", Toast.LENGTH_SHORT).show();
                    askPermissions();
                }
            }
        });

        // Prints to the console that the activity finished loading
        Log.i(TAG, "MainActivity loaded.");
    }

    /**         --onRequestPermissionsResult(...)--
     *   After the permission is either granted or denied, this callback is called.
     *   If granted, the boolean associated
     *
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_BLUETOOTH: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    enabledBT = true;
                return;
            }
            case MY_PERMISSIONS_REQUEST_BLUETOOTH_ADMIN:
            {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    enabledBTA = true;
                return;
            }
            case MY_PERMISSIONS_REQUEST_COARSE_LOC:
            {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    enabledCL = true;
                return;
            }
        }
    }

    private void askPermissions()
    {
        // Asks for Bluetooth Permission
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.BLUETOOTH)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.BLUETOOTH}, MY_PERMISSIONS_REQUEST_BLUETOOTH);
        }
        else
            enabledBT = true;

        // Asks for Bluetooth Admin Permission
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.BLUETOOTH_ADMIN)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.BLUETOOTH_ADMIN}, MY_PERMISSIONS_REQUEST_BLUETOOTH_ADMIN);
        }
        else
            enabledBTA = true;

        // Asks for Coarse Location Permission
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_COARSE_LOC);
        }
        else
            enabledCL = true;
    }
}
