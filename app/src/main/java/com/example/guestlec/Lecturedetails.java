package com.example.guestlec;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lecturedetails extends AppCompatActivity {
  ArrayList<String> personNames;
    ArrayList<String> personImages;
    ArrayList<String> personcomments;
  TextView professor,lecture,date,time,venue;
  ImageButton book_lecture,locate_venue;
    RecyclerView recyclerView;
  private DatabaseReference dref,dref2;
  DataSnapshot dataSnapshot7;
  String lecture_id;
  Bundle b;
    String prof;
    String lec;
    String d;
    String t;
    String v;
    FirebaseAuth fAuth;
    String currentFirebaseUser;
    ImageButton add_comment;
    GoogleSignInAccount acct;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lecture);
        final Map notemap=new HashMap<>();
        personImages=new ArrayList<>();
        personNames=new ArrayList<>();
        personcomments=new ArrayList<>();
         acct = GoogleSignIn.getLastSignedInAccount(Lecturedetails.this);
        dref2=FirebaseDatabase.getInstance().getReference("Comments");

        add_comment=findViewById(R.id.add_comment);
         recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        // set a LinearLayoutManager with default vertical orientation
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        // call the constructor of CustomAdapter to send the reference and data to Adapter
        b=getIntent().getExtras();


        add_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog dialogBuilder = new AlertDialog.Builder(Lecturedetails.this).create();
                LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                View dialogView = inflater.inflate(R.layout.custom_dialog, null);

                final EditText editText = (EditText) dialogView.findViewById(R.id.edt_comment);
                Button button1 = (Button) dialogView.findViewById(R.id.buttonSubmit);
                Button button2 = (Button) dialogView.findViewById(R.id.buttonCancel);

                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogBuilder.dismiss();
                    }
                });
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // DO SOMETHINGS
                        dref = FirebaseDatabase.getInstance().getReference().child("Comments");
                        notemap.put("comments",editText.getText().toString());

                            SharedPreferences pref=getSharedPreferences("emailprefs",MODE_PRIVATE);
                            notemap.put("email",pref.getString("email"," "));
                            notemap.put("image_url",pref.getString("imageurl"," "));
                            notemap.put("lecture",b.getString("topic"));


                        dref.push().setValue(notemap);
                        dialogBuilder.dismiss();



                    }
                });

                dialogBuilder.setView(dialogView);
                dialogBuilder.show();

            }






        });

        dref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int i=0;

                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){

                    if(String.valueOf(dataSnapshot1.child("lecture").getValue()).equals(b.getString("topic"))){
                    personNames.add(String.valueOf(dataSnapshot1.child("email").getValue()));
                    personcomments.add(String.valueOf(dataSnapshot1.child("comments").getValue()));


                    if(String.valueOf(dataSnapshot1.child("image_url").getValue()).equals(" ")){
                        personImages.add(String.valueOf(R.drawable.person1));


                    }else{
                        personImages.add(String.valueOf(dataSnapshot1.child("image_url").getValue()));
                    }

                }
                    i+=1;
                }

                CustomAdapter customAdapter = new CustomAdapter(Lecturedetails.this, personNames,personImages,personcomments);
                recyclerView.setAdapter(customAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


         // set the Adapter to RecyclerView

        book_lecture=findViewById(R.id.book);
        professor = findViewById(R.id.lecturer);
        lecture = findViewById(R.id.subject);
        venue = findViewById(R.id.Venue);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        locate_venue=findViewById(R.id.locate_venue);
        professor.setText("Professor: " + b.getString("professor"));
        lecture.setText("Lecture: " + b.getString("topic"));
        date.setText("Date: " + b.getString("date"));
        venue.setText("Venue: " + b.getString("venue"));
        time.setText("Time: " + b.getString("time"));




        book_lecture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(Lecturedetails.this,SeatDetails.class);
                i.putExtra("LectureName",lecture.getText().toString());
                startActivity(i);


            }
        });

        locate_venue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String place_code=b.getString("venue").toLowerCase();
                String coordinates="0,0";
                switch(place_code)
                {
                    case "ab1":
                        coordinates="10.9005357,76.9023769";
                        break;
                    case "ab2":
                        coordinates="10.9039061,76.8987575";
                        break;
                    case "ab3":
                        coordinates="10.9039061,76.8987575";
                        break;
                    default:

                        System.out.println("no match");
                }
                String param ="google.navigation:q="+coordinates;

                Uri gmmIntentUri = Uri.parse(param);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        }



}



