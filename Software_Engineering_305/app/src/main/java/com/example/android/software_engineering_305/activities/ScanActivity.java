package com.example.android.software_engineering_305.activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.example.android.software_engineering_305.R;
import com.example.android.software_engineering_305.services.BluetoothService;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
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
    private Button              connectButton;
    private TextView            connectPrompt;

    private String              connectionAddress;
    private BluetoothAdapter    mBluetoothAdapter;
    private BluetoothSocket     mmSocket;
    private BluetoothDevice     mmDevice;
    private OutputStream        mmOutputStream;
    private InputStream         mmInputStream;
    private ArrayAdapter<String> addressAdapter;
    private List<String>        myList = new ArrayList<>();

    /**              --onCreate(...)--
     *   Executes when the ScanActivity is launched.
     *
     *  Main responsibility is to...
     * -Get all paired devices and load them into device list
     * -Allow the user to choose a paired device and connect to it
     * -On successful connection, DeviceActivity will launch
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Sets up the activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        mContext = this;

        // Sets up the text view
        connectPrompt = findViewById(R.id.text_connect);

        // Sets up the paired device list
        mDeviceList = findViewById(R.id.device_list);
        getPairedDevices();
        if(myList.size() > 0)
        {
            // Creates a list adapter with address strings, then sets that to list
            addressAdapter = new ArrayAdapter<String>(this, R.layout.listitem_device, myList);
            mDeviceList.setAdapter(addressAdapter);
        }
        else
            Log.i(TAG, "Paired Devices array error.");

        mDeviceList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                connectionAddress = adapterView.getItemAtPosition(i).toString();
                connectPrompt.setText("Connect to " + connectionAddress + "?");
            }
        });

        // Sets up the connect button
        connectButton = findViewById(R.id.connect_button);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                // Checks to see if you've selected an address to connect to
                if(connectionAddress != null)
                {
                    BluetoothService.connect(mContext, connectionAddress);
                }
            }
        });

        Log.e(TAG, "ScanActivity loaded.");
    }

    /**                             --getPairedDevices()--
     *
     *  Bluetooth serial communication seems to need the device to be paired to your
     *  phone before communication starts. This activity enables bluetooth if it's disabled,
     *  then gets a list of each bluetooth address in your paired devices.
     */
    void getPairedDevices()
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
                Log.e(TAG, "Paired device found: " + device.getAddress());
                myList.add(device.getAddress());
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

}
