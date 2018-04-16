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
        String L_STEPPER_SPEED = "L101";
        String L_PITCH_MIN = "L131";
        String L_PITCH_RANGE = "L132";
        String L_ROT_ANGLE = "L111";
        String L_CYCLE_MODE = "L201";
        String L_LIGHT_THRES = "L221";
        String L_YEAR_MONTH_DAY = "L251";
        String L_HOUR_MIN_SEC = "L252";
        String L_WAKE_TIME = "L261";
        String L_SLEEP_TIME = "L262";

        // Spaces already in String
        String S_STEPPER_SPEED = "S101 ";
        String S_PITCH_MIN = "S131 ";
        String S_PITCH_RANGE = "S132 ";
        String S_ROT_ANGLE = "S111 ";
        String S_CYCLE_MODE = "S201 ";
        String S_LIGHT_THRES = "S221 ";
        String S_YEAR_MONTH_DAY = "S251 ";
        String S_HOUR_MIN_SEC = "S252 ";
        String S_WAKE_TIME = "S261 ";
        String S_SLEEP_TIME = "S262 ";

    }

    // Join the two together with Comands.___ + Options.____

    //TODO: COMMAND: Finish the option interface
    interface Options
    {
        String ONE = "1";
    }
}
