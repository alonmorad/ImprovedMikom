package com.example.alon.mikommeorer;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.h6ah4i.android.widget.verticalseekbar.VerticalSeekBar;
import com.yarolegovich.lovelydialog.LovelyCustomDialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import dmax.dialog.SpotsDialog;

import static java.security.AccessController.getContext;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener  {

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private Location lastLocation;
    private LocationRequest mLocationRequest;
    private static final int MY_PERMISSION_REQUEST_CODE = 2980;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 300193;
    private static int UPDATE_INTERVAL = 5000;
    private static int FASTEST_INTERVAL = 3000;
    private static int DISPLACEMENT = 10;
    private MapServices services;
    private AlertDialog alertDialog;
    private StationPickServices stationPickServices;

    DatabaseReference ref;
    FirebaseFirestore firebaseFirestore;
    GeoFire geoFire;
    Marker myCurrent;
    SharedPreferences sharedPreferences;
    float radius;
    String sound;

    private Station station;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //keeps the screen on
        services = new MapServices();
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ref = FirebaseDatabase.getInstance().getReference("MyLocation");
        stationPickServices=new StationPickServices();
        firebaseFirestore = FirebaseFirestore.getInstance();
        geoFire = new GeoFire(ref);
        VerticalSeekBar mSeekBar = findViewById(R.id.VerticalSeekBar);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { //vertical seekbar
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mMap.animateCamera(CameraUpdateFactory.zoomTo(i), 2000, null);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        setUpLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (checkPlayServices()) {
                        buildGoogleApiClient();
                        createLocationRequest();
                        displayLocation();
                    }
                }
                break;
        }

    }

    private void setUpLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && //checks device location permission if not it requests it else the actions begin
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //request Runtime permission
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, MY_PERMISSION_REQUEST_CODE);
        } else {
            if (checkPlayServices()) {
                buildGoogleApiClient();
                createLocationRequest();
                displayLocation();
            }
        }
    }

    private void displayLocation() { //finds current location of device and zooms in
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (lastLocation != null) {
            final double latitude = lastLocation.getLatitude();
            final double longitude = lastLocation.getLongitude();

            //Update to Firebase (not cloud)
            geoFire.setLocation("You", new GeoLocation(latitude, longitude),
                    new GeoFire.CompletionListener() {
                        @Override
                        public void onComplete(String key, DatabaseError error) {
                            //add Marker
                           if (myCurrent != null)
                                myCurrent.remove(); //remove old Marker
                            /*myCurrent = mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(latitude, longitude))
                                    .title("You"));
                            mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(32.1648052,34.8266926)));*/
                            //Move Camera to this position
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 13.55f));
                        }
                    });


            Log.d("Alon", String.format("Your location was changed: %f / %f ", latitude, longitude));
        } else
            Log.d("Alon", "Can't get your location");

    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL); //קובע כל כמה שניות מתעדכן מיקום המכשיר
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);// קובע את רמת הדיוק של מיקום המכשיר
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);//קובע את חריגת האי דיוק
    }

    private void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    private boolean checkPlayServices() { //checks if device supported
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            else {
                Toast.makeText(this, "This Device Is not Supported", Toast.LENGTH_SHORT).show();
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
       /* final String linechoosed=getIntent().getExtras().getString("data"); //tells which line was chosed in the last activity
        final String stationchoosed=getIntent().getExtras().getString("stationchoosed");
        final double notif_lat=getIntent().getExtras().getDouble("station_location_lat");
        final double notif_lng=getIntent().getExtras().getDouble("station_location_lng");*/
        Intent i = getIntent();
        station = i.getParcelableExtra("station");
        sharedPreferences=getSharedPreferences("settings",MODE_PRIVATE);
        radius=sharedPreferences.getFloat("radius",500);
        sound=sharedPreferences.getString("sound","Default"); //שליפת הגדרות וקביעת ערכים שרירותיים במקרה והתהליך נכשל
        Log.d("radius", Float.toString(radius));
        Log.d("sound", sound);
        Log.d("moses", station.getLinenumber());



        toastMakerForGPSandInternet(); //checks if there is no internet connection
        Callback callback = new Callback<List<Station>>() {
            @Override
            public void onCallback(List<Station> stations) {
                if (getContext()==null)
                    return;
                for (Station station1: stations)
                {
                    if (station1.getLinenumber().equals(station.getLinenumber())) //all the stations with the same line number as the user pick
                    {
                        if (!station1.getName().equals(station.getName()))
                            mMap.addMarker(station1.toMarkerOptions(getContext()).alpha(0.65f));
                        else
                            mMap.addMarker(station1.toMarkerOptions(getContext()).snippet("יעד"));
                    }
                    if (station1.getName().equals(station.getName())) //if the station is the user pick!
                    {
                        mMap.addCircle(new CircleOptions()
                                .center(station1.getLocationLatLng())
                                .radius(radius) //meters
                                .strokeColor(Color.MAGENTA)
                                .fillColor(0x220000FF)
                                .strokeWidth(5.0f));
                    }
                }
            }
        };
        services.getStations(callback);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        //house

       LatLng notification_area = new LatLng(station.getLocation().getLatitude(),station.getLocation().getLongitude()); //gets user station cordinates
        Log.d("Moses", String.format("Your location was changed: %f / %f ", station.getLocation().getLatitude(), station.getLocation().getLongitude()));
        //geoquery, 0.5f=0.5k=500m, radius of circle
        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(notification_area.latitude, notification_area.longitude),
                (radius/1000)); //creates a query circle that his center is user station and radius from settings
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onKeyEntered(String key, GeoLocation location) { //The location of a key now matches the query criteria.
                sendNotification("MikoMeorer", String.format("%s entered the chosen area", key), sound);
            }

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onKeyExited(String key) { //The location of a key no longer matches the query criteria.
                sendNotification("MikoMeorer", String.format("%s in no longer in the chosen area", key), sound);
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) { //The location of a key changed but the location still matches the query criteria.
                Log.d("MOVE", String.format("%s moved within the chosen area [%f/%f]", key, location.latitude, location.latitude));
            }

            @Override
            public void onGeoQueryReady() { //All current data has been loaded from the server and all initial events have been fired.

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {
                Log.e("ERROR", "" + error);
            } //There was an error while performing this query, e.g. a violation of security rules.
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() { //info screen
            @Override
            public void onInfoWindowClick(Marker marker) {
                alertDialog=new SpotsDialog(MapsActivity.this,R.style.StationSearch);
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
                            Intent intent=new Intent(MapsActivity.this,CollapsingLayout.class);
                            intent.putExtra("station", station);
                            startActivity(intent);

                        }
                    }
                };
                stationPickServices.getStations(callback,marker.getTitle());
            }
        });
            }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void sendNotification(String title, String content, String sound) { //אחראית על שליחת ההתראות
//        alarmManager=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(title)
                .setContentText(content);
        NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, MapsActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(contentIntent);
        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL | Notification.FLAG_INSISTENT; //first is when user clicks on notification second is the audio will be repeated until the notification is cancelled or the notification window is opened.
        Log.d("sound2",sound);
        if (sound=="Default")
        notification.defaults|=Notification.DEFAULT_SOUND;
        if (sound=="Basic")
        notification.sound= Uri.parse("android.resource://" + this.getPackageName() + "/" + R.raw.alarm_sound);
        notification.defaults |= Notification.DEFAULT_LIGHTS;
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        manager.notify(new Random().nextInt(), notification); //Post a notification to be shown in the status bar. first is tag second is the notification itself
    }

    @Override
    public void onLocationChanged(Location location) { //device location changed
        lastLocation = location;
        displayLocation();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        displayLocation();
        startLocationUpdates();
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, mLocationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    } //gets called when your app gets disconnected from the Google Play services package (not necessarily the Internet)

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void toastMakerForGPSandInternet() //checks if internet connected and sets toast
    {
        if (!isNetworkConnected()) {
            Toast.makeText(this, "No Internet Connection." , Toast.LENGTH_LONG).show();
        }
    }

    private boolean isNetworkConnected() { //for wifi and data
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
