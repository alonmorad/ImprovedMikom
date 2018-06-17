package com.example.alon.mikommeorer;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import dmax.dialog.SpotsDialog;

public class LineSearch extends AppCompatActivity {
    private ListView listView;
    private EditText etSearch;
    private ArrayAdapter<String> adapter;
    String lines []={"501", "3", "27", "29", "82"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); //no keyboard
        setContentView(R.layout.activity_line_search);
        listView=(ListView)findViewById(R.id.listViewLines);
        etSearch=(EditText)findViewById(R.id.etsearch);
        adapter=new ArrayAdapter<String>(this,R.layout.test,R.id.textView,lines); //
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) { //taking the item that was clicked and moving it with intent
                String lineChoosed=String.valueOf(adapterView.getItemAtPosition(i));
                Intent intent=new Intent(LineSearch.this,StationSearch.class);
                intent.putExtra("data",lineChoosed);
                startActivity(intent);
            }
        });
        etSearch.addTextChangedListener(new TextWatcher() { //text watcher == editable with methods!
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { //runs the instant before the text is changed.

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { //runs during the text change
                LineSearch.this.adapter.getFilter().filter(charSequence); //filters the options on the list
            }

            @Override
            public void afterTextChanged(Editable editable) { //runs immediately after the text is changed.

            }
        });
    }
}
