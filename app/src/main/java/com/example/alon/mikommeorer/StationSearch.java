package com.example.alon.mikommeorer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import static java.security.AccessController.getContext;

public class StationSearch extends AppCompatActivity {
    private ListView listView;
    private EditText etSearch;
    private ArrayAdapter<String> adapter;
    private ListBuilderServices services;
    private StationPickServices stationPickServices;
    private double station_location_lat;
    private double station_location_lng;
    private String stationChoosed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_search);
        listView=(ListView)findViewById(R.id.listViewLines);
        etSearch=(EditText)findViewById(R.id.etsearch);
        services = new ListBuilderServices();
        stationPickServices=new StationPickServices();
        adapter=new ArrayAdapter<String>(this,R.layout.test,R.id.textView);
        final String linechoosed=getIntent().getExtras().getString("data");
        Callback callback = new Callback<List<Station>>() {
            @Override
            public void onCallback(List<Station> stations) {
                if (getContext()==null)
                    return;
                for (Station station: stations)
                {
                    adapter.add(station.getName().toString());

                }
            }
        };
        services.getStations(callback,linechoosed);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                stationChoosed=String.valueOf(adapterView.getItemAtPosition(i));
                Log.d("Stationname",  stationChoosed.toString());
                Callback callback = new Callback<List<Station>>()
                {
                    @Override
                    public void onCallback(List<Station> stations) {
                        if (getContext()==null)
                            return;
                        for (Station station: stations)
                        {
                            station_location_lat=station.getLocation().getLatitude();
                            station_location_lng=station.getLocation().getLongitude();
                        }
                    }
                };
                stationPickServices.getStations(callback,stationChoosed);
                Intent intent=new Intent(StationSearch.this,MapsActivity.class);
                intent.putExtra("station_location_lat",station_location_lat);
                intent.putExtra("station_location_lng",station_location_lng);
                intent.putExtra("stationchoosed",stationChoosed);
                intent.putExtra("data", linechoosed);

                startActivity(intent);
            }
        });
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                StationSearch.this.adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
