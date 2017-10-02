package com.jrfapplications.simplechat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    Button loginButton;
    EditText userText, passText;
    String email, password;
    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authlist;
    TextView register;

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authlist);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (Button)findViewById(R.id.loginButton);
        userText = (EditText)findViewById(R.id.userNameText);
        passText = (EditText)findViewById(R.id.confirmPasswordText);
        register = (TextView)findViewById(R.id.signup);

        auth = FirebaseAuth.getInstance();
        authlist = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    if (user.isEmailVerified()) {
                        finish();
                        Intent profileIntent = new Intent(Login.this, Profile.class);
                        startActivity(profileIntent);
                    }else
                    {
                        Intent verify = new Intent(Login.this, Verification.class);
                        startActivity(verify);
                    }
                }
            }
        };
        
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = userText.getText().toString();
                password = passText.getText().toString();
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(!task.isSuccessful())
                                {
                                    Toast.makeText(Login.this, "Failed: Check you email and password", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Toast.makeText(Login.this, "Logged in as " + auth.getCurrentUser().getDisplayName() , Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regIntent = new Intent(Login.this, Register.class);
                startActivity(regIntent);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(authlist != null)
        {
            auth.removeAuthStateListener(authlist);
        }
    }
}
