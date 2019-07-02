package com.example.vijithamerugumala.thismedicalcenter;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity {
    EditText username,password;
    Button login;
    TextView info;
    Intent intent ;
    Bundle bundle;
    int count = 3;
    String name,email,mobile,time,date ,doctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.etusername);
        password = findViewById(R.id.editTextpassword);
        info = findViewById(R.id.textViewinfo);
        info.setText("No attempts remaining : 3");
        login = findViewById(R.id.buttonlogin);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((username.getText().toString().equals("Admin") )&&(password.getText().toString().equals("1234")))
                {
                    bundle = getIntent().getExtras();
                    name = bundle.getString("Name");
                    email = bundle.getString("Email");
                    mobile = bundle.getString("Mobile");
                    time = bundle.getString("Time");
                    date = bundle.getString("Date");
                    doctor = bundle.getString("Doctor");

                    System.out.println("/////////");
                    System.out.println(name);
                    intent = new Intent(getApplicationContext(),ReceptionistActivity.class);
                          intent.putExtra("Name",name);
                          intent.putExtra("Email",email);
                          intent.putExtra("Mobile",mobile);
                          intent.putExtra("Time",time);
                          intent.putExtra("Date",date);
                          intent.putExtra("Doctor",doctor);
                           startActivity(intent);
                }
                else
                {
                    count--;
                    info.setText("No attempts remaining :"+String.valueOf(count));
                    if (count == 0)
                        login.setEnabled(false);
                }


            }
        });

    }

}
