package com.example.vijithamerugumala.thismedicalcenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ReceptionistActivity extends Activity {
    Button email, sms;
    Intent eintent,sintent;
    Bundle bundle;
    TextView msg;
    String name,mail,mobile,time,date,to,subject,emailbody ,doctor;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receptionist);
      //

        msg = findViewById(R.id.msgtv);
        //msg.setText(String.valueOf(msg));
        bundle = getIntent().getExtras();
        name = bundle.getString("Name");
        mail = bundle.getString("Email");
        mobile = bundle.getString("Mobile");
        time = bundle.getString("Time");
        date = bundle.getString("Date");
        doctor = bundle.getString("Doctor");
        email = findViewById(R.id.email);
        System.out.println(name+mail+mobile+time+date +doctor);
        sms = findViewById(R.id.buttonsms);
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subject ="Your appointment  booked ";
                //+ date + time +with Dr+"
                emailbody = "hello r u working";
                to = mail;
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_EMAIL , new String[]{to});
                intent.putExtra(Intent.EXTRA_SUBJECT , subject);
                intent.putExtra(Intent.EXTRA_TEXT , emailbody);
                intent.setType("message/rfc822");
                startActivity(Intent.createChooser(intent,"sendemail"));
            }
        });
        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    emailbody = "Thank you for booking appointment with "+ " "+doctor+" on "+" " +date + "at"+" " +time;

                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(mobile,null,emailbody,null,null);

                }
                catch(Exception e){
                    Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();

                }
            }
        });
    }
}
