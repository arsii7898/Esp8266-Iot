package com.example.muhammad.homeautomationupdatedsystem;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ApplianceConfigration extends AppCompatActivity {


    LinearLayout layout;
    EditText devicename;
    EditText deviceip;
    EditText maindeviceip;
    EditText devicePin;
    MyDBHandler dbHandler;
    TextView recordsTextView;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    String status;
    String ipAddress;
    String portNumber;
    String tempdeviceip;
    String tempdevicepin;
    String tempmaindeviceip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appliance_configration);
        layout = (LinearLayout) findViewById(R.id.linearlayoutmain);
        devicename = (EditText) findViewById(R.id.editText_DeviceName);
        deviceip = (EditText) findViewById(R.id.editText_DeviceIp);
        devicePin=(EditText)findViewById(R.id.editText_Pin);
        recordsTextView = (TextView) findViewById(R.id.textView5);

        maindeviceip=(EditText)findViewById(R.id.editText_Maindeviceip);
        /* Can pass nulls because of the constants in the helper.
         * the 1 means version 1 so don't run update.
         */
        sharedPreferences = getSharedPreferences("HTTP_HELPER_PREFS", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        status ="";
        ipAddress="";
        portNumber="";
        tempdeviceip ="";
        tempdevicepin="";
        tempmaindeviceip="";

        dbHandler = new MyDBHandler(this, null, null, 1);
    }


    //Print the database


    //add your elements onclick methods.
    //Add a product to the database
    public void addDevice(View view) {
        // dbHandler.add needs an object parameter.
        Devices device = new Devices(devicename.getText().toString(), deviceip.getText().toString(), devicePin.getText().toString(), maindeviceip.getText().toString(), "off?");
       if(devicename.getText().toString().equals("") || deviceip.getText().toString().equals("") || maindeviceip.getText().toString().equals("")||devicePin.getText().toString().equals(""))
       {
           Toast.makeText(this,"Fill all fields properly",Toast.LENGTH_LONG).show();
       }
       else
       {
           if(dbHandler.checkDeviceName(devicename.getText().toString()))
           {
               Toast.makeText(this,"Device name already exists",Toast.LENGTH_LONG).show();
           }

            else {
               dbHandler.addDevice(device);
               devicename.setText("");
               deviceip.setText("");
               maindeviceip.setText("");
               devicePin.setText("");
               Toast.makeText(this, "Device added successfully", Toast.LENGTH_LONG).show();

           }//inner else
       }//outer else

    }

    //Delete items
    public void deleteDevice(View view) {
        // dbHandler delete needs string to find in the db
        if(devicename.getText().toString().equals(""))
        {
            Toast.makeText(this,"Device name is required",Toast.LENGTH_LONG).show();

        }//if
        else {
            if (dbHandler.checkDeviceName(devicename.getText().toString())) {
                String inputText = devicename.getText().toString();
                dbHandler.deleteDevice(inputText);
                devicename.setText("");
                deviceip.setText("");
                maindeviceip.setText("");
                Toast.makeText(this, "Device deleted successfully", Toast.LENGTH_LONG).show();

            }
            else {
                Toast.makeText(this, "Device not found", Toast.LENGTH_LONG).show();
            }//inner else
        }//outer else
    }





}

