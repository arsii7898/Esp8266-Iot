package com.example.muhammad.homeautomationupdatedsystem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SignupActivity extends AppCompatActivity {

    EditText fullname;
    EditText email;
    EditText password;
    MyDBHandler dbHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        fullname=(EditText)findViewById(R.id.edittext_fullname);
        email=(EditText)findViewById(R.id.EditText_emailsignup);
        password=(EditText)findViewById(R.id.EditText_passwordsignup);
        dbHandler = new MyDBHandler(this, null, null, 1);
    }

    public void onRegister(View view)
    {
        try
        {
                 if((fullname.getText().toString().equals("") || email.getText().toString().equals("")) || password.getText().toString().equals(""))
                 {
                     Toast.makeText(this,"Fill all fields properly",Toast.LENGTH_LONG).show();
                 }
                else
                 {
                     if(dbHandler.checkEmail(email.getText().toString()))
                     {
                         Toast.makeText(this,"This email already exist",Toast.LENGTH_LONG).show();
                     }
                     else
                     {
                         dbHandler.addUser(fullname.getText().toString(),email.getText().toString(),password.getText().toString());
                         fullname.setText("");
                         email.setText("");
                         password.setText("");
                         Toast.makeText(this,"Account created successfully",Toast.LENGTH_LONG).show();
                         Intent i=new Intent(this,LoginActivity.class);
                         startActivity(i);
                     }
                 }
        }

        catch (Exception e)
        {
            Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
        }
    }//onregister

  public void onBacktoLogin(View view)
  {
      Intent i=new Intent(this,LoginActivity.class);
      startActivity(i);
  }
}
