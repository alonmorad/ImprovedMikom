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

public class DashBoard extends AppCompatActivity implements View.OnClickListener {
    private TextView txtWelcome;
    private EditText input_new_password;
    private Button btnChangePass, btnLogout, btnToApp;
    private RelativeLayout activity_dashboard;
    AlertDialog alertDialog;

    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        getWindow().setBackgroundDrawableResource(R.drawable.background); //background
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); //no keyboard on start
        txtWelcome = (TextView) findViewById(R.id.dashboard_welcome);
        input_new_password = (EditText) findViewById(R.id.dashboard_new_password);
        btnChangePass = (Button) findViewById(R.id.dashboard_btn_change_pass);
        btnLogout = (Button) findViewById(R.id.dashboard_btn_logout);
        activity_dashboard = (RelativeLayout) findViewById(R.id.activity_dash_board);
        btnToApp=(Button)findViewById(R.id.dashboard_btn_toapp);

        btnChangePass.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        btnToApp.setOnClickListener(this);

        //Init Firebase
        auth = FirebaseAuth.getInstance();
        //Session check
        if (auth.getCurrentUser() != null)
            txtWelcome.setText("Welcome , " + auth.getCurrentUser().getEmail());
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.dashboard_btn_change_pass) {
            String password2 = input_new_password.getText().toString().trim();

            if (TextUtils.isEmpty(password2)) {
                Toast.makeText(this, "Please enter new password", Toast.LENGTH_SHORT).show();
                return;
            }
                changePassword(input_new_password.getText().toString());
        }
        else if (view.getId() == R.id.dashboard_btn_logout)
            logoutUser();
        else
            if (view.getId()==R.id.dashboard_btn_toapp)
                startActivity(new Intent(DashBoard.this,LineSearch.class));


    }

    private void changePassword(String newpassword) {
        alertDialog=new SpotsDialog(DashBoard.this,R.style.Dashboard);
        alertDialog.show();
        FirebaseUser user = auth.getCurrentUser();
        user.updatePassword(newpassword).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                alertDialog.dismiss();
                if (task.isSuccessful()) {
                    Toast.makeText(DashBoard.this, "Password changed!", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(DashBoard.this, "Password not changed. Please try again!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onBackPressed(){
        //prevent from going back to login screen
    }

    private void logoutUser() {
        auth.signOut();
        if(auth.getCurrentUser() == null)
        {
            startActivity(new Intent(DashBoard.this,Login.class));
            finish();
        }
    }
}
