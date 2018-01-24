package com.example.alon.mikommeorer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Map extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        getWindow().setBackgroundDrawableResource(R.drawable.background); //background
    }
}
