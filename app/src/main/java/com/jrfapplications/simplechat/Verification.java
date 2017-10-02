package com.jrfapplications.simplechat;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Verification extends AppCompatActivity {

    FirebaseAuth auth;
    Button sendEmail;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        sendEmail = (Button)findViewById(R.id.emailBtn);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!user.isEmailVerified()) {
                    user.sendEmailVerification()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Verification.this, "Email sent", Toast.LENGTH_SHORT).show();
                                        auth.signOut();
                                        finish();
                                        Intent profileIntent = new Intent(Verification.this, Login.class);
                                        startActivity(profileIntent);
                                    }
                                }
                            });
                }
            }
        });
    }
}
