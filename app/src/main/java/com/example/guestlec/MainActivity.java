package com.example.guestlec;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guestlec.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    TextView signup;
    Button normal_sign_in;
    EditText email,pass;
    SignInButton google_signin;
    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN=0;
    private FirebaseAuth.AuthStateListener authStateListener;
    FirebaseAuth firebaseAuth;
    SharedPreferences preferences;

    @Override
    protected void onStop() {
    firebaseAuth.signOut();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth=FirebaseAuth.getInstance();

        signup=findViewById(R.id.tvSignIn);
        normal_sign_in=findViewById(R.id.btnSignIn);
        email=findViewById(R.id.atvEmailLog);
        pass=findViewById(R.id.atvPasswordLog);


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,RegistrationActivity.class);
                startActivity(i);
            }
        });

        google_signin=findViewById(R.id.sign_in_button);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        google_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();

            }
        });

        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user= firebaseAuth.getCurrentUser();
                if(user!=null)
                {
                    SharedPreferences uid=getSharedPreferences("uid",MODE_PRIVATE);
                    SharedPreferences.Editor editor= uid.edit();
                    editor.putString("uid",user.getUid());
                    editor.apply();
                    Toast.makeText(MainActivity.this,"Login Successful!",Toast.LENGTH_SHORT).show();
                    Intent i=new Intent(MainActivity.this,Home.class);
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(MainActivity.this,"Please Login",Toast.LENGTH_SHORT).show();
                }
            }
        };
        normal_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String email_id=email.getText().toString();
                String password=pass.getText().toString();


                if(email_id.trim().isEmpty()) {
                    email.setError("Cannot be empty!");
                    email.requestFocus();
                }
                if(password.trim().isEmpty())
                {
                    pass.setError("Cannot be empty!");
                    pass.requestFocus();
                }
                if(!email_id.trim().isEmpty() && !password.trim().isEmpty()){
                    firebaseAuth.signInWithEmailAndPassword(email_id,password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                SharedPreferences pref=getSharedPreferences("emailprefs",MODE_PRIVATE);
                                SharedPreferences.Editor editor=pref.edit();
                                editor.putString("email",email_id);
                                editor.commit();
                                Intent i=new Intent(MainActivity.this,Home.class);
                                startActivity(i);
                            }
                            else
                            {
                                Toast.makeText(MainActivity.this,"Login Error! Please Login again!",Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });


    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);

            SharedPreferences pref=getSharedPreferences("emailprefs",MODE_PRIVATE);
            SharedPreferences.Editor editor=pref.edit();
            editor.putString("email",acct.getEmail());
            editor.putString("imageurl",acct.getPhotoUrl().toString());
            editor.commit();
            // Signed in successfully, show authenticated UI.
            Intent intent=new Intent(MainActivity.this,Home.class);
            startActivity(intent);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("ERROR", "signInResult:failed code=" + e.getStatusCode());
        }
    }
public String getemail(){
        return email.getText().toString();
}



}
