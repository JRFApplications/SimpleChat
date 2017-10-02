package com.jrfapplications.simplechat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class Profile extends AppCompatActivity {

    TextView logout, welcomeMsgText, userEmail;
    ImageView profileImage;
    Uri uriprofileimage;
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        logout = (TextView)findViewById(R.id.logout);
        welcomeMsgText = (TextView)findViewById(R.id.welcomeMsg);
        profileImage = (ImageView)findViewById(R.id.profileImage);
        userEmail = (TextView)findViewById(R.id.emailText);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Loading Profile...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();

        uriprofileimage = user.getPhotoUrl();
        welcomeMsgText.setText(user.getDisplayName());
        userEmail.setText(user.getEmail());
        Picasso.with(this).load(uriprofileimage).into(profileImage);

        progress.dismiss();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                finish();
                Intent login = new Intent(Profile.this, Login.class);
                startActivity(login);
            }
        });
    }
}
