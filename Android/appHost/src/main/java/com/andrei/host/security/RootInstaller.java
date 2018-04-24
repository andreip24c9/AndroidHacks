package com.andrei.host.security;


import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class RootInstaller {
    public boolean install(String strApkPath) {
        boolean result = false;
        DataOutputStream dataOutputStream = null;
        BufferedReader errorReader = null;
        try {
            Process process = Runtime.getRuntime().exec("su");
            dataOutputStream = new DataOutputStream(process.getOutputStream());
            String command = "pm install -r " + strApkPath + "\n";
            dataOutputStream.write(command.getBytes(Charset.forName("utf-8")));
            dataOutputStream.flush();
            dataOutputStream.writeBytes("exit\n");
            dataOutputStream.flush();
            process.waitFor();
            errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = errorReader.readLine()) != null) {
                sb.append(line);
            }
            String output = sb.toString();
            Log.d("Installer", "install msg is " + output);
            if (!output.contains("Failure")) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (dataOutputStream != null) {
                    dataOutputStream.close();
                }
                if (errorReader != null) {
                    errorReader.close();
                }
            } catch (IOException ignored) {

            }
        }
        return false;
    }
}

