package com.example.muhammad.homeautomationupdatedsystem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
   MyDBHandler myDBHandler;
    EditText username;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username=(EditText)findViewById(R.id.edittext_emailLogin);
        password=(EditText)findViewById(R.id.edittext_passwordLogin);
        myDBHandler=new MyDBHandler(this, null, null, 1);


    }

    public void onSignUp(View view)
    {
        Intent intent=new Intent(this,SignupActivity.class);
        startActivity(intent);
    }

    public void onLogin(View view)
    {
        try{

           if(myDBHandler.checkEmail(username.getText().toString()))
           {
               String temppassword=myDBHandler.getPassword(username.getText().toString());
              if(temppassword.equals(password.getText().toString()))
              {

                  username.setText("");
                  password.setText("");
                  Intent i=new Intent(this,HomeActivity.class);
                  startActivity(i);

                  Toast.makeText(this,"Login successfully",Toast.LENGTH_LONG).show();
              }
               else
              {
                  Toast.makeText(this,"Invalid username or password",Toast.LENGTH_LONG).show();
              }
           }
            else
           {
               Toast.makeText(this,"Invalid username or password",Toast.LENGTH_LONG).show();
           }
        }
        catch (Exception e)
        {
            Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
        }
    }
}
