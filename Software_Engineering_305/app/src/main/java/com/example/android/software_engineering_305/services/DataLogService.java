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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
        } catch (IOException e)
        {
            e.printStackTrace();
        } finally
        {
            try {
                if (br != null)br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    public boolean deleteValues(String name)
    {
        for (String csvName : getNames())
        {
            if (name == csvName)
            {
                try
                {
                    File tempFile = new File(csv.getAbsolutePath() + ".tmp");

                    BufferedReader br = new BufferedReader(new FileReader(csv));
                    PrintWriter pw = new PrintWriter(new FileWriter(tempFile));

                    String line = null;

                    //Read from the original file and write to the new
                    //unless content matches data to be removed.
                    while ((line = br.readLine()) != null) {

                        if (!line.trim().substring(0,line.indexOf(",")).equals(name)) {
                            pw.println(line);
                            pw.flush();
                        }
                    }
                    pw.close();
                    br.close();

                    //Delete the original file
                    if (!csv.delete()) {
                        System.out.println("Could not delete file");
                        return false;
                    }

                    //Rename the new file to the filename the original file had.
                    if (!tempFile.renameTo(csv))
                    {
                        System.out.println("Could not rename file");
                        return false;
                    }
                    csv = tempFile;
                    return true;
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    return false;
                }
            }
            break;
        }
        return true;
    }

    public boolean writeValues(String[] values)
    {
        try
        {
            deleteValues(values[0]);
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