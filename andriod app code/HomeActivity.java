package com.example.muhammad.homeautomationupdatedsystem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    LinearLayout linearlayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        linearlayout=(LinearLayout)findViewById(R.id.linearlayout_Vocationmode);

    }

    public void onApplianceConfigration(View view)
    {
        Intent intent=new Intent(this,ApplianceConfigration.class);
        startActivity(intent);
        Toast.makeText(this,"Appliance Configration",Toast.LENGTH_LONG).show();
    }
    public void onApplianceStatus(View view)
    {
        Intent intent=new Intent(this,ApplianceActivity.class);
        startActivity(intent);
    }
    public void onVocationMode(View view)
    {
        Intent intent=new Intent(this,VocationModeActivity.class);
        startActivity(intent);
        Toast.makeText(this,"Vocation Mode",Toast.LENGTH_LONG).show();
    }
    public  void onAutoMode(View view)
     {
        Intent intent=new Intent(this,AutoModeActivity.class);
        startActivity(intent);

     }
}

