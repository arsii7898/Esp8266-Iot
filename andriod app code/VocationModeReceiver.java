package com.example.muhammad.homeautomationupdatedsystem;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Muhammad on 06/06/2017.
 */



public class VocationModeReceiver extends BroadcastReceiver {
    String vocationModeDeviceName;
    String vocationModeDeviceIp;
    String vocationModeMainDeviceIp;
    String vocationModeDevicePin;
    String vocationModeStatus;
    String portNumber;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    MyDBHandler dbHandler;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onReceive(Context context, Intent intent) {
        String state = intent.getExtras().getString("extra");
        portNumber = "80";
        Log.e("MyActivity", "In the receiver with " + state);
       // Toast.makeText(context, "i am in receiver", Toast.LENGTH_LONG).show();
        vocationMode(context);
    }


    public void vocationMode(Context c) {
        try {
            sharedPreferences = c.getSharedPreferences("HTTP_HELPER_PREFS", Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
            dbHandler = new MyDBHandler(c, null, null, 1);
            vocationModeDeviceName = dbHandler.getVocationModeDeviceName();
            vocationModeStatus = dbHandler.getVocationModeStatus();
            vocationModeDeviceIp = dbHandler.getDeviceIp(vocationModeDeviceName);
            vocationModeDevicePin = dbHandler.getDevicePin(vocationModeDeviceName);
            vocationModeMainDeviceIp = dbHandler.getMainDeviceIP(vocationModeDeviceName);

            if (vocationModeStatus.equals("on?")) {
                vocationModeStatus = "off?";
                dbHandler.updateVocationModeStatus(vocationModeDeviceName, vocationModeStatus);
                Toast.makeText(c, "Appliance off", Toast.LENGTH_LONG).show();
                 Log.v("testing","off appliances");
                sendCommand(c);
            } else {
                vocationModeStatus = "on?";
                dbHandler.updateVocationModeStatus(vocationModeDeviceName, vocationModeStatus);
                Toast.makeText(c, "Appliance on", Toast.LENGTH_LONG).show();
                Log.v("testing","on appliance");
                sendCommand(c);

            }
        } catch (Exception e) {
            Log.e("error", e.toString());
        }
    }

    public void sendCommand(Context c) {


        // ipAddress = editTextIPAddress.getText().toString().trim();
        //portNumber = editTextPortNumber.getText().toString().trim();
        try {
            String data = vocationModeDeviceIp + "?" + vocationModeDevicePin;
            portNumber = "80";
            editor.putString("PREF_IP_ADDRESS", vocationModeMainDeviceIp);
            editor.putString("PREF_IP_ADDRESS", portNumber);
            editor.commit(); // save the IP and PORT
            if (vocationModeMainDeviceIp.length() > 0 && portNumber.length() > 0) {
                new HttpRequstAsyncTask(
                       c.getApplicationContext() , vocationModeStatus, vocationModeMainDeviceIp, portNumber, data
                ).execute();


            }
        } catch (Exception e) {
            Log.e("error", e.toString());
        }

    }

    public class HttpRequstAsyncTask extends AsyncTask<Void, Void, Void> {


        // declare variables needed
        private String requestReply, ipAddress, portNumber;
        private Context context;
        private AlertDialog alertDialog;
        private String parameter;
        private String parameterValue;



        public String sendRequest(String parameterValue, String ipAddress, String portNumber, String parameterName) {
            String serverResponse = "ERROR";

            try {

                HttpClient httpclient = new DefaultHttpClient();
                URI website = new URI("http://" + ipAddress + ":" + portNumber + "/?" + parameterName + "=" + parameterValue);
                HttpGet getRequest = new HttpGet();
                getRequest.setURI(website);

                HttpResponse response = httpclient.execute(getRequest); // execute the request
                InputStream content = null;
                content = response.getEntity().getContent();
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        content
                ));
                serverResponse = in.readLine();

                content.close();
            } catch (ClientProtocolException e) {

                serverResponse = e.getMessage();
                e.printStackTrace();
            } catch (IOException e) {

                serverResponse = e.getMessage();
                e.printStackTrace();
            } catch (URISyntaxException e) {

                serverResponse = e.getMessage();
                e.printStackTrace();
            }

            return serverResponse;
        }
        public HttpRequstAsyncTask(Context context, String parameterValue, String ipAddress, String portNumber, String parameter) {
      /*  this.context = context;

        alertDialog = new AlertDialog.Builder(this.context)
                .setTitle(" Response From Main Hub")
                .setCancelable(true)
                .create();
*/
            this.ipAddress = ipAddress;
            this.parameterValue = parameterValue;
            this.portNumber = portNumber;
            this.parameter = parameter;
        }

        @Override
        protected Void doInBackground(Void... voids) {
       /* //alertDialog.setMessage("Data sent, waiting for reply from server...");
        if (!alertDialog.isShowing()) {
            alertDialog.show();
        }*/
            requestReply = sendRequest(parameterValue, ipAddress, portNumber, parameter);
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
      /*  alertDialog.setMessage(requestReply);
        if (!alertDialog.isShowing()) {
            alertDialog.show(); // show dialog
        }*/
        }


        @Override
        protected void onPreExecute() {
       /*alertDialog.setMessage("");
        if (!alertDialog.isShowing()) {
            alertDialog.show();
        }*/
        }

    }

}
