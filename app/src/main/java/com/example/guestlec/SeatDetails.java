package com.example.guestlec;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.BuddhistCalendar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SeatDetails extends AppCompatActivity {
    Button confirm;
    EditText personname,rollno;
    static int i=0;
    Bundle b;
    private DatabaseReference dref;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_details);
        confirm=findViewById(R.id.confirm_seat);
       final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref=getSharedPreferences("emailprefs",MODE_PRIVATE);
                final Map notemap = new HashMap();
                b=getIntent().getExtras();
                rollno=findViewById(R.id.roll_num);
                personname=findViewById(R.id.person_name);
                notemap.put("Username",personname.getText().toString());
                notemap.put("LectureName",b.getString("LectureName"));
                notemap.put("Rollno",rollno.getText().toString());
                notemap.put("Email",pref.getString("email",""));



                dref=FirebaseDatabase.getInstance().getReference().child("User_Lectures");
                dref.push().setValue(notemap);

                addNotification();

                personname.setText(" ");
                rollno.setText(" ");
                //newLectureref.child(notemap.get("LectureName").toString()).setValue(notemap);


            }
        });
    }

    private void addNotification() {
        // Builds your notification
        Intent notificationIntent = new Intent(this, SeatDetails.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification builder = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Booking confirmation")
                .setContentIntent(contentIntent)
                .setContentText(b.getString("LectureName")+" Lecture booked for "+personname.getText().toString()).build();

        // Creates the intent needed to show the notification

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder);
        Toast.makeText(this,b.getString("LectureName")+" Lecture booked for "+personname.getText().toString(),Toast.LENGTH_LONG).show();
    }





}
