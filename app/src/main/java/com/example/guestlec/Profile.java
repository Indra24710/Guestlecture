package com.example.guestlec;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.Map;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class Profile extends Fragment {
    TextView name,email,id;
    ImageView photo;
    Button sign_out;
    GoogleSignInClient mGoogleSignInClient;
    Button book_lecture;
    Intent intent;
    private FirebaseAuth.AuthStateListener authStateListener;
    DatabaseReference dref;

    FirebaseAuth firebaseUser;
    private ProgressDialog progressDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.profile,container,false);
        name=view.findViewById(R.id.Name);
        book_lecture=view.findViewById(R.id.Addlecture);
        email=view.findViewById(R.id.email);

        intent = new Intent(getActivity(), AddLecture.class);

     //   id=view.findViewById(R.id.username);
        photo=view.findViewById(R.id.profilephoto);
     //   sign_out=view.findViewById(R.id.button);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);


        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getContext());
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
         //   String personId = acct.getId();
            String personPhoto = acct.getPhotoUrl().toString();
            name.setText(personName);
            email.setText(personEmail);
        //    id.setText(personId);

            Glide
                    .with(getContext()).load(personPhoto)
                    .override(450, 400)
                    .centerCrop()
                    .into(photo);

        }else{

            SharedPreferences pref=getContext().getSharedPreferences("emailprefs",MODE_PRIVATE);

            String email_id=pref.getString("email","");
            String image_url=pref.getString("imageurl","");
            email.setText(email_id);

            Glide
                    .with(getContext()).load(image_url)
                    .override(450, 400)
                    .centerCrop()
                    .into(photo);
            name.setText(" ");






        }

return view;
    }
}
