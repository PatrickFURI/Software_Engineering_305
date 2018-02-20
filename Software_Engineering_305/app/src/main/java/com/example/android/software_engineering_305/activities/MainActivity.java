package com.example.android.software_engineering_305.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.android.software_engineering_305.R;

/**                 -- MainActivity --
 *
 *  This is the activity (screen) that the app first loads.
 *
 */
public class MainActivity extends AppCompatActivity
{
    private static final String TAG = "MainActivity";
    private Context mContext;
    private Button  startButton;

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
}
