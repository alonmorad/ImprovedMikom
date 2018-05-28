package com.example.alon.mikommeorer;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    private GridLayout gridLayout;
    private CardView busCardView, changeCardView, settingsCardView;
    private TextView welcome, username;
    private FirebaseAuth auth;
    private ImageButton menu;
    private boolean success =false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        gridLayout = findViewById(R.id.mainGrid);
        busCardView = findViewById(R.id.bus);
        changeCardView = findViewById(R.id.change);
        settingsCardView = findViewById(R.id.settings);
        menu=findViewById(R.id.popmenu);
        welcome=findViewById(R.id.welcome);
        username = findViewById(R.id.username);
        auth = FirebaseAuth.getInstance();
        busCardView.setOnClickListener(this);
        changeCardView.setOnClickListener(this);
        settingsCardView.setOnClickListener(this);
        menu.setOnClickListener(this);
        checkSystemWritePermission();
        if (auth.getCurrentUser() != null)
            username.setText(auth.getCurrentUser().getEmail());


    }

    @Override
    public void onClick(View view) {
        if (view == busCardView) {
            startActivity(new Intent(HomeActivity.this, LineSearch.class));
        }
        if (view == changeCardView) {
            startActivity(new Intent(HomeActivity.this, ChangePassword.class));
        }
        if (view == settingsCardView) {
            startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
        }
        if (view == menu)
        {
            PopupMenu popupMenu = new PopupMenu(HomeActivity.this,menu);
            popupMenu.getMenuInflater().inflate(R.menu.popup_menu,popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    auth.signOut();
                    if (auth.getCurrentUser() == null) {
                        startActivity(new Intent(HomeActivity.this, Login.class));
                        finish();
                    }
                    return false;
                }
            });
            popupMenu.show();
        }
    }

    private void checkSystemWritePermission() {
        Boolean value;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            value = Settings.System.canWrite(this);
            if (value) {
                success = true;
                startService(new Intent(this, BatteryService.class));
            } else {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
                startActivityForResult(intent, 1000);
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Boolean value = Settings.System.canWrite(this);
                if (value) {
                    success = true;
                    startService(new Intent(this, BatteryService.class));
                }
            }
        }
    }
}
