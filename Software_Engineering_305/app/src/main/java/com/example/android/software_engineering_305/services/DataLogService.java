package com.example.android.software_engineering_305.services;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Console;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class DataLogService{
    public DataLogService()
    {
        try
        {
            csv = new File("data.csv");
            if (!csv.exists()) {
                csv.createNewFile();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private File csv;

    public String[] getNames()
    {
        ArrayList<String> names = new ArrayList<String>();

        BufferedReader br = null;
        try {
            String sCurrentLine;
            br = new BufferedReader(new FileReader(csv.getAbsoluteFile()));
            while ((sCurrentLine = br.readLine()) != null) {
                names.add(sCurrentLine.substring(0,sCurrentLine.indexOf(",")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return names.toArray(new String[names.size()]);
    }

    public String[] getValues(String name)
    {
        BufferedReader br = null;
        try {
            String sCurrentLine;
            br = new BufferedReader(new FileReader(csv.getAbsoluteFile()));
            while ((sCurrentLine = br.readLine()) != null) {
                if (sCurrentLine.substring(0,sCurrentLine.indexOf(",")) == name)
                {
                    return sCurrentLine.split(",");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    public boolean writeValues(String[] values)
    {
        try
        {
            String content = "";
            for (String val : values)
            {
                content = content + val + ",";
            }
            content = content.substring(0,content.length()-2);

            FileWriter fw = new FileWriter(csv.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }
}