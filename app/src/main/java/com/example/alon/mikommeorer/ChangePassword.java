package com.example.alon.mikommeorer;

import android.app.AlertDialog;
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
import com.google.firebase.auth.FirebaseUser;

import dmax.dialog.SpotsDialog;

public class ChangePassword extends AppCompatActivity implements View.OnClickListener {
    private EditText input_email;
    private Button btnResetPass;
    private TextView btnBack;
    private RelativeLayout activity_forgot;
    AlertDialog alertDialog;

    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        getWindow().setBackgroundDrawableResource(R.drawable.bg); //background
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); //no keyboard on start
        input_email = (EditText)findViewById(R.id.forgot_email);
        btnResetPass = (Button)findViewById(R.id.change);
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
            startActivity(new Intent(this,HomeActivity.class));
            finish();
        }
        if (view.getId() == R.id.change) {
            String password2 = input_email.getText().toString().trim();

            if (TextUtils.isEmpty(password2)) {
                Toast.makeText(this, "Please enter new password", Toast.LENGTH_SHORT).show();
                return;
            }
            changePassword(input_email.getText().toString());
        }
    }

    private void changePassword(String newpassword) {
        alertDialog = new SpotsDialog(ChangePassword.this, R.style.StationSearch);
        alertDialog.show();
        FirebaseUser user = auth.getCurrentUser();
        user.updatePassword(newpassword).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                alertDialog.dismiss();
                if (task.isSuccessful()) {
                    Toast.makeText(ChangePassword.this, "Password changed!", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(ChangePassword.this, "Password not changed. Please try again!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
