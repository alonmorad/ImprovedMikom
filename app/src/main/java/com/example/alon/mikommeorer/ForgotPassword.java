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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import dmax.dialog.SpotsDialog;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {
    private EditText input_email;
    private Button btnResetPass;
    private TextView btnBack;
    private RelativeLayout activity_forgot;
    AlertDialog alertDialog;

    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        getWindow().setBackgroundDrawableResource(R.drawable.background4); //background
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); //no keyboard on start
        input_email = (EditText)findViewById(R.id.forgot_email);
        btnResetPass = (Button)findViewById(R.id.forgot_btn_reset);
        btnBack = (TextView)findViewById(R.id.forgot_btn_back);
        activity_forgot = (RelativeLayout)findViewById(R.id.activity_forgot_password);

        btnResetPass.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        auth = FirebaseAuth.getInstance();


    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.forgot_btn_back)
        {
            startActivity(new Intent(this,Login.class));
            finish();
        }
        else  if(view.getId() == R.id.forgot_btn_reset)
        {
            String Email=input_email.getText().toString().trim();

            if (TextUtils.isEmpty(Email))
            {
                Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
                return;
            }
            resetPassword(input_email.getText().toString());
        }

    }

    private void resetPassword( final String email) {
        alertDialog=new SpotsDialog(ForgotPassword.this,R.style.Forgot_pass);
        alertDialog.show();
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        alertDialog.dismiss();
                        if(task.isSuccessful())
                        {
                            Toast.makeText(activity_forgot.getContext(),"We have sent password reset link to email: "+ email ,Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(activity_forgot.getContext(),"Failed to send mail",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
