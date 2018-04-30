package com.example.alon.mikommeorer;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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

import dmax.dialog.SpotsDialog;

public class Login extends AppCompatActivity implements View.OnClickListener {
    Button btnLogin;
    AutoCompleteTextView email,password;
    TextView signUp, forgotPass;
    RelativeLayout activity_main;
    AlertDialog alertDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().setBackgroundDrawableResource(R.drawable.bg); //background
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

        //Check already session , if ok-> ChangePassword
        if(firebaseAuth.getCurrentUser() != null)
            startActivity(new Intent(Login.this,HomeActivity.class));
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.login_btn_forgot_password)
        {
            startActivity(new Intent(Login.this,ForgotPassword.class));
            finish();
        }
        else if(view.getId() == R.id.login_btn_signup)
        {
            startActivity(new Intent(Login.this,SignUp.class));
            finish();
        }
        else if(view.getId() == R.id.login_btn_login)
        {
            String email2=email.getText().toString().trim();
            String password2=password.getText().toString().trim();

            if (TextUtils.isEmpty(email2))
            {
                Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password2))
            {
                Toast.makeText(this, "Please enter Password", Toast.LENGTH_SHORT).show();
                return;
            }
            loginUser(email.getText().toString(),password.getText().toString());
        }
    }

    private void loginUser(String email, final String password) {
        alertDialog=new SpotsDialog(Login.this,R.style.StationSearch);
        alertDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        alertDialog.dismiss();
                        if (task.isSuccessful())
                        {
                            finish();
                            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        }
                        else
                            Toast.makeText(Login.this, "Wrong Email or Password. Please try again", Toast.LENGTH_SHORT).show();
                    }
                });
}}
