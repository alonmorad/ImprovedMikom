package com.example.alon.mikommeorer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class CollapsingLayout extends AppCompatActivity {
    private ImageView imageView;
    private TextView textView,textView2,textView3;
    private Station station;

    private FirebaseStorage storage=FirebaseStorage.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collapsing_layout);

        Intent i = getIntent();
        station = i.getParcelableExtra("station"); //getting station info with intent parcelable
        StorageReference gsReference = storage.getReferenceFromUrl(station.getPicture_url()); //ref for firebase storage
        Toolbar toolbar = findViewById(R.id.toolbartest);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        imageView=findViewById(R.id.imagechange);
        textView=findViewById(R.id.tv1);
        textView2=findViewById(R.id.tv2);
        textView3=findViewById(R.id.tv3);
        textView.setText(station.getName());
        textView2.setText(station.getDescription());
        textView3.setText(station.getHours());
        Glide.with(this).using(new FirebaseImageLoader()).load(gsReference).into(imageView); //downloading the pic into imageview
    }
}
