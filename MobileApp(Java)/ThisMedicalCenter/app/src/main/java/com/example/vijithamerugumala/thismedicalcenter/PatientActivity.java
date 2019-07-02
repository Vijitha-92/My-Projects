package com.example.vijithamerugumala.thismedicalcenter;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import java.util.Calendar;

import static android.content.ContentValues.TAG;

public class PatientActivity extends Activity {
    EditText name,mobile,time,email ,displaydate;;
    Spinner spinner;
    Button callbtn,websitebtn,bookbtn;
   // DatePicker datePicker;
    Intent call,website ,book;
    private DatePickerDialog.OnDateSetListener listener;

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    String doctor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);
        name = findViewById(R.id.editTextname);
        mobile = findViewById(R.id.editTextmobile);
        time = findViewById(R.id.editTexttime);
        email = findViewById(R.id.editTextemail);

        spinner = findViewById(R.id.spinnerdoc);

        callbtn = findViewById(R.id.buttoncall);
        websitebtn = findViewById(R.id.buttonwebsite);
        bookbtn = findViewById(R.id.buttonbook);
         displaydate = findViewById(R.id.editTextdate);
        final String[] Doctors = getResources().getStringArray(R.array.Doctors);
        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(this,R.array.Doctors,android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              //  Toast.makeText(getApplicationContext(),"you selected \t"+Doctors[position],Toast.LENGTH_LONG).show();
                setDoctor(Doctors[position]);            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
               Toast.makeText(getApplicationContext(),"No Doctor selected",Toast.LENGTH_LONG).show();
            }
        });
        displaydate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year =  calendar.get(Calendar.YEAR);
                int month =  calendar.get(Calendar.MONTH);
                int day =  calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(PatientActivity.this,android.R.style.Theme_Material_Dialog_MinWidth,listener,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                Log.d(TAG,"on Date dd/mm/yyyy :"+dayOfMonth+ "/"+month+"/" +year);
                String date = dayOfMonth+ "/" +month+" /"+year;
                displaydate.setText(date);
            }
        };
        callbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:(02)94721207"));
                if(call.resolveActivity(getPackageManager())!= null)
                    startActivity(call);
                else
                    Log.d("2","Not working");


            }
        });
        websitebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.google.com");
                website = new Intent(Intent.ACTION_VIEW, uri);
                if(website.resolveActivity(getPackageManager())!= null)
                    startActivity(website);
                else
                    Log.d("1","Not working");

            }
        });
        bookbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                book = new Intent(getApplicationContext(),MainActivity.class);
               book.putExtra("Name",name.getText().toString());
               book.putExtra("Email",email.getText().toString());
                book.putExtra("Mobile",mobile.getText().toString());
                book.putExtra("Time",time.getText().toString());
                book.putExtra("Date",displaydate.getText().toString());
                book.putExtra("Doctor",doctor);


                //startActivity(getApplicationContext(),LoginActivity.class);
                //startActivity(book);
                setResult(Activity.RESULT_OK ,book);
                finish();
            }
        });

    }
}
