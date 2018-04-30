package com.example.alon.mikommeorer;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import dmax.dialog.SpotsDialog;

public class SignUp extends AppCompatActivity implements View.OnClickListener {
    Button sign_up;
    TextView btnLogin,btnForgotPass;
    EditText input_email,input_pass;
    RelativeLayout activity_sign_up;
    AlertDialog alertDialog;

    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getWindow().setBackgroundDrawableResource(R.drawable.bg); //background
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); //no keyboard on start
        sign_up = (Button)findViewById(R.id.btn_register);
        btnLogin = (TextView)findViewById(R.id.Signup_btn_login);
        btnForgotPass = (TextView)findViewById(R.id.signup_btn_forgot_pass);
        input_email = (EditText)findViewById(R.id.signup_email);
        input_pass = (EditText)findViewById(R.id.signup_password);
        activity_sign_up = (RelativeLayout)findViewById(R.id.activity_sign_up);

        sign_up.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnForgotPass.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.Signup_btn_login){
            startActivity(new Intent(SignUp.this,Login.class));
            finish();
        }
        else if(view.getId() == R.id.signup_btn_forgot_pass){
            startActivity(new Intent(SignUp.this,ForgotPassword.class));
            finish();
        }
        else if(view.getId() == R.id.btn_register){
            String email=input_email.getText().toString().trim();
            String password=input_pass.getText().toString().trim();

            if (TextUtils.isEmpty(email))
            {
                Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password))
            {
                Toast.makeText(this, "Please enter Password", Toast.LENGTH_SHORT).show();
                return;
            }
            signUpUser(input_email.getText().toString(),input_pass.getText().toString());
        }

    }

    private void signUpUser(String email, String password) { //method for putting user data in firebase
        alertDialog=new SpotsDialog(SignUp.this,R.style.StationSearch);
        alertDialog.show();
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        alertDialog.dismiss();
                        if (task.isSuccessful()) {
                            Toast.makeText(SignUp.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(getApplicationContext(),Login.class));
                        }
                        else
                        {
                            Toast.makeText(SignUp.this, "Could not register. please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
