package com.example.muhammad.homeautomationupdatedsystem;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class AutoModeActivity extends AppCompatActivity {

    EditText sensorPin;
    Spinner spinner;
    MyDBHandler dbHandler;
    String aModeDeviceName;
    String aModeDevicePin;
    String aModeDeviceIp;
    String aModeMainDeviceIp;
    String aModeDeviceStatus;
    String portNumber;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_mode);
        spinner = (Spinner) findViewById(R.id.spinner_automode);
        dbHandler = new MyDBHandler(this, null, null, 1);
        aModeDeviceName = "";
        aModeDeviceIp = "";
        aModeMainDeviceIp = "";
        aModeDevicePin = "";
        aModeDeviceStatus = "";
        portNumber = "";
        sharedPreferences = getSharedPreferences("HTTP_HELPER_PREFS", Context.MODE_PRIVATE);
      try {
          editor = sharedPreferences.edit();
          ArrayList<String> list = dbHandler.getDevicesName();
          ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, list);
          spinner.setAdapter(adapter);
      }
      catch (Exception e)
      {
          Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
      }

    }

    public void onStart(View view) {

        try {
            aModeDeviceName = (spinner.getSelectedItem().toString());
            aModeDeviceIp = dbHandler.getMainDeviceIP(aModeDeviceName);
            aModeMainDeviceIp = dbHandler.getMainDeviceIP(aModeDeviceName);
                aModeDeviceStatus = "on?";
               // Toast.makeText(this, aModeDeviceName + aModeDevicePin, Toast.LENGTH_LONG).show();
                dbHandler.updateAutoModeData(aModeDeviceName, aModeDevicePin, aModeDeviceStatus);
                 sendCommand(view);


        }
        catch (Exception e)
        {
            Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
        }

        }


    public void onStop(View view) {

       try {

           aModeDeviceStatus = "off?";
           sendCommand(view);
       }
       catch (Exception e)
       {
           Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
       }
    }


    public void sendCommand(final View view) {
        String data = aModeDeviceIp + "?" + aModeDevicePin;
        portNumber = "80";
        editor.putString("PREF_IP_ADDRESS", aModeMainDeviceIp);
        editor.putString("PREF_IP_ADDRESS", portNumber);
        editor.commit(); // save the IP and PORT
        if (aModeDeviceIp.length() > 0 && portNumber.length() > 0) {
            new HttpRequestAsyncTask(
                    view.getContext(), aModeDeviceStatus, aModeMainDeviceIp, portNumber, data
            ).execute();


        }
    }
}