package com.example.android.software_engineering_305.application;

/**
 * Created by Andrew on 2/13/18.
 */

public enum BluetoothAction
{
    connect ("example.android.software_engineering_305.application.connect"),
    disconnect ("example.android.software_engineering_305.application.disconnect");

    private final String mAction;

    BluetoothAction(String action) {
        mAction = action;
    }

    public static BluetoothAction getAction(String action) {
        switch (action) {
            case "example.android.software_engineering_305.application.connect":
                return connect;
            case "example.android.software_engineering_305.application.disconnect":
                return disconnect;
            default:
                return null;
        }
    }

    public String toString() {
        return this.mAction;
    }
}
