package com.login.aimware.loginapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {


    private EditText Name;
    private EditText Password;
    private TextView Info;
    private Button Login;
    private int counter = 5;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private TextView forgotPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Name = (EditText)findViewById(R.id.etName);
        Password = (EditText)findViewById(R.id.etUserPassword);
        Info = (TextView)findViewById(R.id.tvInfo);
        Login = (Button)findViewById(R.id.btnlogin);
        Info.setText("No of attempts remaining: 5");
        TextView userRegistration = (TextView) findViewById(R.id.tvRegister);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        forgotPassword = (TextView)findViewById(R.id.tvForgotPassword);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user != null){
            finish();
            startActivity(new Intent(MainActivity.this, SecondActivity.class));
        }


        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(Name.getText().toString(),Password.getText().toString());
            }
        });

        userRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,RegistrationActivity.class));
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,PasswordActivity.class));
            }
        });
    }

    private void validate(String username, String userpass) {
        if(username.isEmpty() || userpass.isEmpty())
        {
            Toast.makeText(this,"Please Enter all the details",Toast.LENGTH_SHORT).show();
        }
        else {
            progressDialog.setMessage("Please Wait! Until We Verify Your Credentials!");
            progressDialog.show();
            firebaseAuth.signInWithEmailAndPassword(username, userpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        //Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        //startActivity(new Intent(MainActivity.this, SecondActivity.class));
                        checkEmailVerification();
                    } else {
                        
                        Toast.makeText(MainActivity.this, "Login Failed ,Please Try again!", Toast.LENGTH_SHORT).show();
                        //counter--;  perform operation while setting the text.
                        Info.setText("No of attempts remaining: " + counter--);
                        progressDialog.dismiss();
                        if (counter == 0) {
                            Login.setEnabled(false);
                        }
                    }

                }
            });
        }
    }
    private void checkEmailVerification()
    {
        FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();
        boolean emailflag = firebaseUser.isEmailVerified();
        if(emailflag)
        {
            startActivity(new Intent(MainActivity.this,SecondActivity.class));
        }
        else
        {
            Toast.makeText(this,"Please Verify Your Email Address",Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }
}
