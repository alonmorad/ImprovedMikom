package com.example.alon.mikommeorer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class StationSearch extends AppCompatActivity {
    private ArrayAdapter<String> adapter;
    private EditText editText;
    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_search);
        listView=(ListView)findViewById(R.id.listViewLines);
        editText=(EditText)findViewById(R.id.etsearch);
        adapter=new ArrayAdapter<String>(this, R.layout.activity_station_search,)
    }
}
