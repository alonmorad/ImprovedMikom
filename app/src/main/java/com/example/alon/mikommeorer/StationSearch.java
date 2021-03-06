package com.example.alon.mikommeorer;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import dmax.dialog.SpotsDialog;

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
    private AlertDialog alertDialog;
    private final String TAG = "Document:";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_search);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); //no keyboard
        listView=findViewById(R.id.listViewLines);
        etSearch=findViewById(R.id.etsearch);
        services = new ListBuilderServices();
        stationPickServices=new StationPickServices();
        adapter=new ArrayAdapter<String>(this,R.layout.test,R.id.textView);
        final String linechoosed=getIntent().getExtras().getString("data");
        Callback callback = new Callback<List<Station>>() {
            @Override
            public void onCallback(List<Station> stations) { //building the list with the stations with the same linenumber in database
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
                alertDialog=new SpotsDialog(StationSearch.this,R.style.StationSearch);
                alertDialog.show();
                Callback callback = new Callback<List<Station>>()
                {
                    @Override
                    public void onCallback(List<Station> stations) {
                        alertDialog.dismiss();
                        if (getContext()==null)
                            return;
                        for (Station station: stations)
                        {
                            station_location_lat=station.getLocation().getLatitude();
                            station_location_lng=station.getLocation().getLongitude();
                            Intent intent=new Intent(StationSearch.this,MapsActivity.class);
                            Log.d("locationstation", String.format("Your location was changed: %f / %f ", station_location_lat, station_location_lng));
                            /*intent.putExtra("station_location_lat",station_location_lat);
                            intent.putExtra("station_location_lng",station_location_lng);
                            intent.putExtra("stationchoosed",stationChoosed);
                            intent.putExtra("data", linechoosed);*/
                            intent.putExtra("station", station);
                            startActivity(intent);

                        }
                    }
                };
                stationPickServices.getStations(callback,stationChoosed);
            }
        });
        etSearch.addTextChangedListener(new TextWatcher() { //text watcher == editable with methods!
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { ////runs the instant before the text is changed.

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { ////runs during the text change
                StationSearch.this.adapter.getFilter().filter(charSequence); ////filters the options on the list
            }

            @Override
            public void afterTextChanged(Editable editable) { //runs immediately after the text is changed.

            }
        });
    }
}
