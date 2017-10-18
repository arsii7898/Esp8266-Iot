package com.example.muhammad.homeautomationupdatedsystem;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Random;

public class ApplianceActivity extends AppCompatActivity {
    String deviceIp;
    String devicePin;
    String maindeviceip;
    String devicestatus;
    String portNumber;
    MyDBHandler dbHandler;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    LinearLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appliance);
        deviceIp="";
        devicePin="";
        maindeviceip="";
        devicestatus="";
        portNumber="";
        layout = (LinearLayout) findViewById(R.id.linearlayout_verticalappliances);
        sharedPreferences = getSharedPreferences("HTTP_HELPER_PREFS",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        dbHandler = new MyDBHandler(this, null, null, 1);
        deviceDataToButton();
    }

    public void deviceDataToButton()
    {
        try {


            String dbString = dbHandler.getDataOfDevices();
            if (!dbString.isEmpty()) {
                String[] tempdevicedata = dbString.split("\n");
                String temp = "";
                for (int i = 0; i < tempdevicedata.length; i++) {

                    if (tempdevicedata[i] != null) {

                        String[] nam = tempdevicedata[i].split(",");
                       temp = dbHandler.getDeviceStatus(nam[0]);
                        createButton(nam[0]);
                        nam[0] = null;
                        nam[1] = null;


                    }//if
                }//for

            }//if
        }//try
        catch(Exception e)
        {
            Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
        }
    }//dataofbuttons

    public void createButton(String name)
    {
        devicestatus = dbHandler.getDeviceStatus(name);
        Random r = new Random();
        int btnid = (r.nextInt(80) + 65);
        final Button b = new Button(this);
        b.setTag(name);
        b.setText(name);
        b.setId(btnid);
        if (devicestatus.equals("off?")) {
            b.setBackgroundColor(Color.RED);
        } else if (devicestatus.equals("on?")) {
            b.setBackgroundColor(Color.GREEN);
        }

        b.setTransformationMethod(null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(400,200);

        params.setMargins(500,50,0,0);//ltrb
        ShapeDrawable shapedrawable = new ShapeDrawable();

        b.setLayoutParams(params);
        try {
            // b.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            b.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ColorDrawable buttonColor = (ColorDrawable) b.getBackground();
                    int colorId = buttonColor.getColor();
                    if (colorId == Color.RED) {
                        b.setBackgroundColor(Color.GREEN);
                        devicestatus = "on?";

                        devicePin = dbHandler.getDevicePin(b.getText().toString());
                        deviceIp = dbHandler.getDeviceIp(b.getText().toString());
                        maindeviceip = dbHandler.getMainDeviceIP(b.getText().toString());
                        Log.v("data1", deviceIp + devicePin + maindeviceip + "--------------on----------------");
                        dbHandler.updateDeviceStatus(b.getText().toString(), "on?");
                        Toast.makeText(getApplicationContext(), "Appliance on", Toast.LENGTH_SHORT).show();
                        sendCommand(v);
                    } else {
                        b.setBackgroundColor(Color.RED);
                        devicestatus = "off?";
                        devicePin = dbHandler.getDevicePin(b.getText().toString());
                        deviceIp = dbHandler.getDeviceIp(b.getText().toString());
                        maindeviceip = dbHandler.getMainDeviceIP(b.getText().toString());
                        Log.v("data1", deviceIp + devicePin + maindeviceip + "--------------off----------------");
                        dbHandler.updateDeviceStatus(b.getText().toString(), "off?");
                        Toast.makeText(getApplicationContext(), "Appliance off", Toast.LENGTH_SHORT).show();
                        sendCommand(v);

                    }

                }
            });
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
        layout.addView(b);

    }

    public void sendCommand(final View view) {


        // ipAddress = editTextIPAddress.getText().toString().trim();
        //portNumber = editTextPortNumber.getText().toString().trim();
        String data = deviceIp + "?" + devicePin;
        portNumber = "80";
        editor.putString("PREF_IP_ADDRESS", maindeviceip);
        editor.putString("PREF_IP_ADDRESS", portNumber);
        editor.commit(); // save the IP and PORT
        if (maindeviceip.length() > 0 && portNumber.length() > 0) {
            new HttpRequestAsyncTask(
                    view.getContext(), devicestatus, maindeviceip, portNumber, data
            ).execute();


        }
    }




}
