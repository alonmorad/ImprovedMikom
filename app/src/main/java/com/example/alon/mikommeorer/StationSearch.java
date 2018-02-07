package com.example.alon.mikommeorer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class StationSearch extends AppCompatActivity {
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_search);
        ListView listView=(ListView)findViewById(R.id.listViewLines);
        ArrayList<String> arrayLines=new ArrayList<>();
        arrayLines.addAll(Arrays.asList(getResources().getStringArray(R.array.array_lines)));
        adapter=new ArrayAdapter<String>(StationSearch.this, android.R.layout.simple_list_item_1,arrayLines);
    }
}
