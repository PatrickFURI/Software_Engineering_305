package com.example.android.software_engineering_305.activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.software_engineering_305.R;
import com.example.android.software_engineering_305.services.BluetoothService;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


/**
 * Created by Andrew on 2/13/18.
 */

public class ScanActivity extends AppCompatActivity
{
    private static final String TAG = "ScanActivity";
    private Context             mContext;
    private ListView            mDeviceList;
    private Button              connectButton, disconnectButton;
    private SimpleAdapter       listAdapter;
    private String              connectionAddress;
    private BluetoothAdapter    mBluetoothAdapter;
    private ArrayList<HashMap<String,String>> myList = new ArrayList<HashMap<String,String>>();

    /**              --onCreate(...)--
     *   Executes when the ScanActivity is launched.
     *
     *  Main responsibility is to...
     * -Get all paired devices and load them into device list
     * -Allow the user to choose a paired device and connect to it
     * -On successful connection, SettingsActivity will launch
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Sets up the activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        getSupportActionBar().setHomeButtonEnabled(false);
        mContext = this;

        // Sets up the paired device list
        mDeviceList = findViewById(R.id.device_list);
        getPairedDevices();
        if(myList.size() > 0)
        {
            // Creates a simple adapter with address strings and name strings, then sets that to list
            listAdapter = new SimpleAdapter(this, myList, R.layout.activity_scan_list_items, new String[] { "address","name"},
                    new int[] {R.id.address, R.id.name});
            mDeviceList.setAdapter(listAdapter);
        }
        else
            Log.i(TAG, "Paired Devices array error.");

        mDeviceList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                connectionAddress = ((HashMap<String,String>)adapterView.getItemAtPosition(i)).get("address");
                BluetoothService.connect(mContext, connectionAddress);
                Toast.makeText(mContext, "Connecting...", Toast.LENGTH_SHORT).show();
                //connectPrompt.setText("Connect to " + connectionAddress + "?");
            }
        });

        // Sets up the connect button
        connectButton = findViewById(R.id.connect_button);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(!BluetoothService.connected)
                {
                    Toast.makeText(mContext, "Please select a device.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    BluetoothService.read(mContext);
                    // Leaves this activity, goes to SettingsActivity
                    Intent intent = new Intent(mContext, SettingsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                Log.e(TAG, "Connected: " + BluetoothService.connected);
            }
        });
        disconnectButton = findViewById(R.id.disconnect_button);
        disconnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(BluetoothService.connected)
                {
                    Toast.makeText(mContext, "Disconnecting...", Toast.LENGTH_SHORT).show();
                    BluetoothService.disconnect(mContext);
                }
                else
                    Toast.makeText(mContext, "Not Currently Connected", Toast.LENGTH_SHORT).show();
            }
        });


        Log.i(TAG, "ScanActivity loaded.");
    }

    /**                             --getPairedDevices()--
     *
     *  Bluetooth serial communication seems to need the device to be paired to your
     *  phone before communication starts. This activity enables bluetooth if it's disabled,
     *  then gets a list of each bluetooth address in your paired devices.
     */
    private void getPairedDevices()
    {
        // Gets the Bluetooth adapter, used to perform any Bluetooth task
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter == null)
        {
            Log.d(TAG, "Bluetooth adaptor is NULL.");
        }

        // Checks if Bluetooth is enabled, enables it if not
        if(!mBluetoothAdapter.isEnabled())
        {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 0);
        }

        // Gets paired devices, lists the addresses, then stores them in array
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if(pairedDevices.size() > 0)
        {
            for(BluetoothDevice device : pairedDevices) {
                HashMap<String,String> btDevice = new HashMap<>(); //need to use a hashmap for the multiline display
                Log.i(TAG, "Paired device found: " + device.getAddress());
                if(device.getName().substring(0,3).equals("LS-")) {
                    btDevice.put("name", device.getName());
                    btDevice.put("address", device.getAddress());
                    myList.add(btDevice);
            }
        }
    }


    /**                     --onBackPressed()--
     *  Returns to the parent activity (MainActivity) if the back button is pressed
     */
    @Override
    public void onBackPressed()
    {
        Log.e(TAG, "Back pressed. Navigating to " + getParentActivityIntent());
        Intent intent = this.getParentActivityIntent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        super.onBackPressed();
    }

    /**               --onOptionsItemSelected(...)--
     *
     * Same functionality as pressing the back button. This will open the parent activity (MainActivity)
     *
     * @param item: Item from the action bar that was selected, in this case the back button
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.e(TAG, "Home item pressed. Navigating to " + getParentActivityIntent());
                Intent intent = this.getParentActivityIntent();
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
        }
        return true;
    }

}
