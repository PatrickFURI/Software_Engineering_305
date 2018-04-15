package com.example.android.software_engineering_305.application;

/**
 * Created by Andrew on 3/5/18.
 */

public interface CommandInterface
{
    //TODO: COMMAND: Finish the command interface
    interface Commands
    {
        int NUM_COMMANDS = 10;
        String CYCLE_MODE = "L201";          // Displays "Laser Scarecrow Debug Available!" if true
    }

    // Join the two together with Comands.___ + Options.____

    //TODO: COMMAND: Finish the option interface
    interface Options
    {
        String ONE = "1";
    }
}
