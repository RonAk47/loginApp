package com.login.aimware.loginapp;

import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {

    private EditText user_name;
    private EditText user_email;
    private EditText user_password;
    private Button signupbtn;
    private TextView already;
    private FirebaseAuth firebaseAuth;
    private EditText userAge;
    private ImageView userProfilePic;
    private String email, name, age, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setupUIViews();

        firebaseAuth = FirebaseAuth.getInstance();
        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate())
                {
                    //If true then upload data to database
                    private String userEmail = user_email.getText().toString().trim();
                    private String userPass = user_password.getText().toString().trim();
                    firebaseAuth.createUserWithEmailAndPassword(userEmail,userPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                              sendUserData();
                                firebaseAuth.signOut();
                                finish();
                                Toast.makeText(RegistrationActivity.this,"Registration Successfull, Verification mail sent",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegistrationActivity.this,MainActivity.class));
                            }else
                            {
                                Toast.makeText(RegistrationActivity.this,"Registration Failed",Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });

        already.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this,MainActivity.class));
            }
        });
    }

    private void setupUIViews()
    {
        user_name = (EditText)findViewById(R.id.etUserName);
        user_email = (EditText)findViewById(R.id.etUserEmail);
        user_password = (EditText)findViewById(R.id.etUserPassword);
        signupbtn = (Button)findViewById(R.id.btnRegister);
        already = (TextView) findViewById(R.id.tvAlready);
        userAge = (EditText) findViewById(R.id.etAge);
        userProfilePic = (ImageView) findViewById(R.id.ivProfile);
    }
    private boolean validate()
    {
        //boolean result = false;
        name = user_name.getText().toString();
        email = user_email.getText().toString();
        password = user_password.getText().toString();
        age = userAge.getText().toString();
        if(name.isEmpty() || email.isEmpty() || password.isEmpty() || age.isEmpty())
        {
            Toast.makeText(this,"Some Required fields are missing, please fill all the details to continue.",Toast.LENGTH_SHORT).show();
        }
        else
        {
            return true;
        }
        return false;
    }
    private void sendEmailVerification()
    {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser!= null)
        {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        firebaseAuth.signOut();
                    }

                }
            });
        }
    }
    private void sendUserData()
    {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference(firebaseAuth.getUid());
        UserProfile userProfile = new UserProfile(age, email, name);
        myRef.setValue(userProfile);
        sendEmailVerification();

    }
}

