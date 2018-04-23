package com.example.alon.mikommeorer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    EditText radius;
    Button save;
    RadioButton silent,basic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        radius=findViewById(R.id.radiuset);
        save=findViewById(R.id.save);
        basic=findViewById(R.id.basic);
        silent=findViewById(R.id.silent);
        save.setOnClickListener(this);
        basic.setOnClickListener(this);
        silent.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view==save)
        {
            SharedPreferences sharedPreferences=getSharedPreferences("settings",MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putInt("radius", Integer.parseInt(radius.getText().toString()));
            editor.apply();
            startActivity(new Intent(SettingsActivity.this,HomeActivity.class));
        }
    }
}
