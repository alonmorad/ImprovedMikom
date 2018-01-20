package com.example.alon.mikommeorer;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity implements View.OnClickListener {
    Button btnLogin;
    AutoCompleteTextView email,password;
    TextView signUp, forgotPass;
    RelativeLayout activity_main;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().setBackgroundDrawableResource(R.drawable.background); //background
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); //no keyboard on start

        btnLogin = (Button)findViewById(R.id.login_btn_login);
        email = (AutoCompleteTextView) findViewById(R.id.userEmail);
        password = (AutoCompleteTextView) findViewById(R.id.userPassword);
        signUp = (TextView)findViewById(R.id.login_btn_signup);
        forgotPass = (TextView)findViewById(R.id.login_btn_forgot_password);
        activity_main = (RelativeLayout)findViewById(R.id.activity_main);

        signUp.setOnClickListener(this);
        forgotPass.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();

        //Check already session , if ok-> DashBoard
        if(firebaseAuth.getCurrentUser() != null)
            startActivity(new Intent(Register.this,DashBoard.class));
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.login_btn_forgot_password)
        {
            startActivity(new Intent(Register.this,ForgotPassword.class));
            finish();
        }
        else if(view.getId() == R.id.login_btn_signup)
        {
            startActivity(new Intent(Register.this,SignUp.class));
            finish();
        }
        else if(view.getId() == R.id.login_btn_login)
        {
            loginUser(email.getText().toString(),password.getText().toString());
        }
    }

    private void loginUser(String email, final String password) {
        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful())
                        {
                            if(password.length() < 6)
                            {
                                Toast.makeText(activity_main.getContext(), "pass must be more than 6 chars", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            startActivity(new Intent(Register.this,DashBoard.class));
                        }
                    }
                });
}}
