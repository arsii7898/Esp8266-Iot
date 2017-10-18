package com.example.muhammad.homeautomationupdatedsystem;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class VocationModeActivity extends AppCompatActivity {
    AlarmManager alarmManager;
    private PendingIntent pending_intent;
    private TimePicker vocationmodeTimePicker;
    private TextView vocationmodeTextView;
    private EditText vocationmodetimeEditText;
    private Spinner spinner;

    String portNumber;
    int vocationModeMinutes;
    String vocationModeStartTime;
    String vocationModeDeviceIp;
    String vocationModeMainDeviceIp;
    String vocationModeDevicePin;
    String vocationModeStatus;


    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    VocationModeActivity inst;
    Context context;
    MyDBHandler dbHandler;


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocation_mode);

        this.context = this;
        sharedPreferences = context.getSharedPreferences("HTTP_HELPER_PREFS", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        dbHandler = new MyDBHandler(this, null, null, 1);
        vocationmodeTextView = (TextView) findViewById(R.id.textView_showdata);
        final Intent myIntent = new Intent(VocationModeActivity.this, VocationModeReceiver.class);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        final Calendar calendar = Calendar.getInstance();
        vocationmodeTimePicker = (TimePicker) findViewById(R.id.vocation_ModeTimepicker);
        vocationmodetimeEditText = (EditText) findViewById(R.id.editText_VocationModeTime);
        Button start_vocation_mode = (Button) findViewById(R.id.button_Start);
        vocationModeMinutes = 0;
        vocationModeStartTime = "";
        vocationModeDeviceIp = "";
        vocationModeMainDeviceIp = "";
        vocationModeDevicePin = "";
        vocationModeStatus = "off?";
        spinner = (Spinner) findViewById(R.id.spinner);
        try {
        ArrayList<String> list = dbHandler.getDevicesName();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, list);
        spinner.setAdapter(adapter);
        if ((dbHandler.getVocationModeStatus()).equals("")) {

            dbHandler.addVocationModeData("", vocationModeStartTime, Integer.toString(vocationModeMinutes), vocationModeStatus);
        }


            start_vocation_mode.setOnClickListener(new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {

                    String tempDevicename = spinner.getSelectedItem().toString();

                    if ((vocationmodetimeEditText.getText().toString()).equals("")) {
                        Toast.makeText(getApplicationContext(), "Enter the Vocation mode minutes", Toast.LENGTH_LONG).show();
                    } else {
                        calendar.add(Calendar.SECOND, 3);
                        final int hour = vocationmodeTimePicker.getCurrentHour();
                        final int minute = vocationmodeTimePicker.getCurrentMinute();
                        vocationModeStartTime = (Integer.toString(hour) + ":" + Integer.toString(minute));
                        //declared already vocation mode minutes
                        vocationModeDeviceIp = dbHandler.getDeviceIp(spinner.getSelectedItem().toString());
                        vocationModeMainDeviceIp = dbHandler.getMainDeviceIP(spinner.getSelectedItem().toString());
                        vocationModeDevicePin = dbHandler.getDevicePin(spinner.getSelectedItem().toString());
                        vocationModeMinutes = Integer.parseInt(vocationmodetimeEditText.getText().toString());

                        vocationModeStatus = "on?";
                        Log.e("MyActivity", "In the receiver with " + hour + " and " + minute);
                        calendar.set(Calendar.HOUR_OF_DAY, vocationmodeTimePicker.getCurrentHour());
                        calendar.set(Calendar.MINUTE, vocationmodeTimePicker.getCurrentMinute());
                        myIntent.putExtra("extra", "yes");

                        pending_intent = PendingIntent.getBroadcast(VocationModeActivity.this, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), vocationModeMinutes * 60 * 1000, pending_intent);
                        setAlarmText("Vocation Mode set to " + hour + ":" + minute);
                        dbHandler.updateVocationModeData(tempDevicename, vocationModeStartTime, Integer.toString(vocationModeMinutes), vocationModeStatus);
                       // sendCommand(v);

                        //  Toast.makeText(getApplicationContext(),spinner.getSelectedItem().toString(),Toast.LENGTH_LONG).show();

                    }


                }
            });//startbutton


            Button stop_vocation_mode = (Button) findViewById(R.id.button_Stop);
            stop_vocation_mode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //alarmManager.cancel(pending_intent);
                    PendingIntent.getBroadcast(context, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT).cancel();
                    vocationModeStatus = "off?";
                    vocationModeDeviceIp = dbHandler.getDeviceIp(spinner.getSelectedItem().toString());
                    vocationModeMainDeviceIp = dbHandler.getMainDeviceIP(spinner.getSelectedItem().toString());
                    vocationModeDevicePin = dbHandler.getDevicePin(spinner.getSelectedItem().toString());
                    dbHandler.updateVocationModeData(spinner.getSelectedItem().toString(), vocationModeStartTime, Integer.toString(vocationModeMinutes), vocationModeStatus);
                    setAlarmText("Vocation Mode Stopped");
                    Log.e("MyActivity", "Vocation mode Stopped");
                    //sendCommand(v);

                }
            });//stopbutton

        }
        catch (Exception e)
        {
             Log.e("error",e.toString());
        }
        }


    public void setAlarmText(String vocationmodetext) {
        vocationmodeTextView.setText(vocationmodetext);
    }


    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.e("MyActivity", "on Destroy");
    }

    public void sendCommand(final View view) {


        // ipAddress = editTextIPAddress.getText().toString().trim();
        //portNumber = editTextPortNumber.getText().toString().trim();
        String data = vocationModeDeviceIp + "?" + vocationModeDevicePin;
        portNumber = "80";
        editor.putString("PREF_IP_ADDRESS", vocationModeMainDeviceIp);
        editor.putString("PREF_IP_ADDRESS", portNumber);
        editor.commit(); // save the IP and PORT
        if (vocationModeMainDeviceIp.length() > 0 && portNumber.length() > 0) {
            new HttpRequestAsyncTask(
                    view.getContext(), vocationModeStatus, vocationModeMainDeviceIp, portNumber, data
            ).execute();


        }

    }

    
}