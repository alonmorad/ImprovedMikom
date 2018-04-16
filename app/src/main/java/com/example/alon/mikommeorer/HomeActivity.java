package com.example.alon.mikommeorer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    private GridLayout gridLayout;
    private CardView busCardView, changeCardView, addressCardView, settingsCardView;
    private TextView logout, welcome, username;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        gridLayout = findViewById(R.id.mainGrid);
        busCardView = findViewById(R.id.bus);
        changeCardView = findViewById(R.id.change);
        addressCardView = findViewById(R.id.address);
        settingsCardView = findViewById(R.id.settings);
        logout = findViewById(R.id.logout);
        welcome=findViewById(R.id.welcome);
        username = findViewById(R.id.username);
        auth = FirebaseAuth.getInstance();
        busCardView.setOnClickListener(this);
        changeCardView.setOnClickListener(this);
        addressCardView.setOnClickListener(this);
        settingsCardView.setOnClickListener(this);
        logout.setOnClickListener(this);
        if (auth.getCurrentUser() != null)
            username.setText(auth.getCurrentUser().getEmail());


    }

    @Override
    public void onClick(View view) {
        if (view==busCardView)
        {
            startActivity(new Intent(HomeActivity.this,LineSearch.class));
        }
        if (view==changeCardView)
        {
            startActivity(new Intent(HomeActivity.this,ChangePassword.class));
        }
        if (view==addressCardView)
        {
            startActivity(new Intent(HomeActivity.this,LineSearch.class));
        }
        if (view==settingsCardView)
        {
            startActivity(new Intent(HomeActivity.this,LineSearch.class));
        }
        if (view==logout)
        {
            auth.signOut();
            if(auth.getCurrentUser() == null)
            {
                startActivity(new Intent(HomeActivity.this,Login.class));
                finish();
            }
        }
    }
}
