package com.jrfapplications.simplechat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Register extends AppCompatActivity {

    Button regsiterButton, uploadImage;
    EditText emailText, passwordText, confirmPassText, displayNameText;
    String email, password, displayName, confirmPassword;
    int GALLERY_INTENT = 100;
    Uri userImage;
    Uri uploadedImage;
    StorageReference storage;
    FirebaseDatabase database;
    DatabaseReference myRef;


    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        regsiterButton = (Button)findViewById(R.id.registerBtn);
        emailText = (EditText)findViewById(R.id.emailText);
        passwordText = (EditText)findViewById(R.id.passwordText);
        displayNameText = (EditText)findViewById(R.id.displayNameText);
        confirmPassText = (EditText)findViewById(R.id.confirmPasswordText);
        uploadImage = (Button)findViewById(R.id.uploadImage);

        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance().getReference();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_INTENT);
            }
        });

        regsiterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = emailText.getText().toString();
                displayName = displayNameText.getText().toString();
                password = passwordText.getText().toString();
                confirmPassword = confirmPassText.getText().toString();

                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(displayName))
                {
                    if (password.equals(confirmPassword)) {
                        final ProgressDialog progress = new ProgressDialog(Register.this);
                        progress.setTitle("Loading");
                        progress.setMessage("Please wait whilst we create your account");
                        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
                        progress.show();
                        auth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            progress.setMessage("Uploading profile image");
                                            final FirebaseUser user = auth.getCurrentUser();
                                            final StorageReference filepath = storage.child("Users").child(user.getUid()).child("Profile Image").child("Image");
                                            filepath.putFile(userImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                                                    //noinspection VisibleForTests
                                                    uploadedImage = taskSnapshot.getDownloadUrl();

                                                    progress.setMessage("Creating Profile");

                                                    UserProfileChangeRequest setProfile = new UserProfileChangeRequest.Builder()
                                                            .setDisplayName(displayName)
                                                            .setPhotoUri(uploadedImage)
                                                            .build();

                                                    user.updateProfile(setProfile).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful())
                                                            {
                                                                myRef.child("Users").setValue(user.getUid());
                                                                progress.dismiss();
                                                                Toast.makeText(Register.this, "Registered", Toast.LENGTH_SHORT).show();
                                                                finish();
                                                                Intent login = new Intent(Register.this, Login.class);
                                                                startActivity(login);
                                                            }
                                                            else
                                                            {
                                                                Toast.makeText(Register.this, "Failed " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                                progress.dismiss();
                                                            }
                                                        }
                                                    });
                                                }
                                            });

                                        } else {
                                            Toast.makeText(Register.this, "Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                    else
                    {
                        Toast.makeText(Register.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(Register.this, "All fields are required", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK)
        {
            userImage = data.getData();
        }

    }
}
