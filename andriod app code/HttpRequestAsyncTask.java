package com.example.muhammad.homeautomationupdatedsystem;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;

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
 * Created by Muhammad on 10/06/2017.
 */

public class HttpRequestAsyncTask extends AsyncTask<Void, Void, Void> {


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
    public HttpRequestAsyncTask(Context context, String parameterValue, String ipAddress, String portNumber, String parameter) {
       this.context = context;

        alertDialog = new AlertDialog.Builder(this.context)
                .setTitle(" Response From Main Hub")
                .setCancelable(true)
                .create();

        this.ipAddress = ipAddress;
        this.parameterValue = parameterValue;
        this.portNumber = portNumber;
        this.parameter = parameter;
    }

    @Override
    protected Void doInBackground(Void... voids) {
       //alertDialog.setMessage("Data sent, waiting for reply from server...");
        if (!alertDialog.isShowing()) {
            alertDialog.show();
        }
        requestReply = sendRequest(parameterValue, ipAddress, portNumber, parameter);
        return null;
    }


    @Override
    protected void onPostExecute(Void aVoid) {
       alertDialog.setMessage(requestReply);
        if (!alertDialog.isShowing()) {
            alertDialog.show(); // show dialog
        }
    }


    @Override
    protected void onPreExecute() {
       alertDialog.setMessage("");
        if (!alertDialog.isShowing()) {
            alertDialog.show();
        }
    }

}

