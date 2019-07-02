package com.example.vijithamerugumala.thismedicalcenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
    private static final int PATIENTCODE = 1;
    private static final int RECEPTIONCODE = 2;
    Button btnpatient, btnreceptionist;
    Intent pintent, rintent;
    Bundle extra;
    Appointment appointment = new Appointment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnpatient = findViewById(R.id.patientbtn);
        btnpatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pintent = new Intent(getApplicationContext(), PatientActivity.class);
                //startActivity(pintent);
                startActivityForResult(pintent, PATIENTCODE);
            }
        });
        btnreceptionist = findViewById(R.id.receptionistBtn);
        btnreceptionist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rintent = new Intent(getApplicationContext(), LoginActivity.class);
                rintent.putExtra("Name",appointment.getName());
                rintent.putExtra("Email",appointment.getEmail());
                rintent.putExtra("Mobile",appointment.getMobile());
                rintent.putExtra("Time",appointment.getTime());
               rintent.putExtra("Date",appointment.getDate());
                rintent.putExtra("Doctor",appointment.getDoctor());
                startActivity(rintent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode ,Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        switch(requestCode)
        {
           case PATIENTCODE:
            if(resultCode == Activity.RESULT_OK)
                Toast.makeText(getApplicationContext(),data.getExtras().getString("Name"),Toast.LENGTH_LONG).show();
                appointment.setName(data.getStringExtra("Name"));

                //System.out.println(name);
               //name = data.getString("Name");
              appointment.setEmail(data.getStringExtra("Email"));
               appointment.setMobile(data.getStringExtra("Mobile"));
               appointment.setTime(data.getStringExtra("Time"));
              appointment.setDate(data.getStringExtra("Date"));
               appointment.setDoctor(data.getStringExtra("Doctor"));
            }


        }
    }

