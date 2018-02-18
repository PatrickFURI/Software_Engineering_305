package com.example.android.software_engineering_305.services;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.software_engineering_305.application.BluetoothAction;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Andrew on 2/16/18.
 *
 * Remember to add service to manifest
 */

public class BluetoothService extends Service
{
    private static final String EXTRA_DEVICE = "tex_tronics.wbl.uri.ble.sg.device";
    private static final String TAG = "BluetoothService";
    private boolean connected = false;
    private Context mContext;
    private BluetoothDevice mDevice;
    private BluetoothSocket serialSocket;
    private InputStream serialInputStream;
    private OutputStream serialOutputStream;

    // Find out how to receiver
    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this;
        //registerReceiver(mBLEUpdateReceiver, new IntentFilter(BluetoothLeConnectionService.INTENT_FILTER_STRING));
        //bindService(new Intent(this, BluetoothLeConnectionService.class), mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startID) {
        if (intent == null) {
            return START_REDELIVER_INTENT;
        }

        String deviceAddress = intent.getStringExtra(EXTRA_DEVICE);
        BluetoothAction action = BluetoothAction.getAction(intent.getAction());

        switch (action)
        {
            case connect:
                connect(deviceAddress);
                break;
            case disconnect:
                //disconnect(deviceAddress);
                break;
        }
        return START_REDELIVER_INTENT;
    }

    public void connect(String deviceAddress)
    {
        if(connected)
        {
            Log.e(TAG,"Failed: Connection request while already connected");
            return;
        }
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter== null || !mBluetoothAdapter.isEnabled())
        {
            Log.e(TAG,"Failed: Bluetooth Adapter Error");
            return;
        }

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if(pairedDevices.size() > 0)
        {
            for(BluetoothDevice device : pairedDevices) {
                Log.e(TAG, "Paired device found: " + device.getAddress());
                if(device.getAddress().equals(deviceAddress))
                    mDevice = device;
            }
        }
        if(mDevice == null)
        {
            Log.e(TAG,"Failed: Device could not be found");
            return;
        }

        try{
            // Standard SerialPortService ID
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
            serialSocket = mDevice.createRfcommSocketToServiceRecord(uuid);
            serialSocket.connect();
            serialInputStream = serialSocket.getInputStream();
            serialOutputStream = serialSocket.getOutputStream();

            connected = true;
            Log.e(TAG,"Success! Connected to " + mDevice.getName());
        }
        catch (IOException e)
        {
            serialSocket = null;
            serialInputStream = null;
            serialOutputStream = null;
            e.printStackTrace();
        }

    }

    public static void connect(Context context, String deviceAddress) {
        Intent intent = new Intent(context, BluetoothService.class);
        intent.putExtra(EXTRA_DEVICE, deviceAddress);
        intent.setAction(BluetoothAction.connect.toString());
        context.startService(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {return null;}
}
