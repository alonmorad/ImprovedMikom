package com.example.alon.mikommeorer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;

import com.yarolegovich.lovelydialog.LovelyChoiceDialog;
import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    Button save,radiusbtn,soundbtn,reset;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getWindow().setBackgroundDrawableResource(R.drawable.bg);
        sharedPreferences=getSharedPreferences("settings",MODE_PRIVATE);
        save=findViewById(R.id.save);
        reset=findViewById(R.id.reset);
        radiusbtn=findViewById(R.id.radiusbutton);
        soundbtn=findViewById(R.id.soundbutton);
        save.setOnClickListener(this);
        radiusbtn.setOnClickListener(this);
        soundbtn.setOnClickListener(this);
        reset.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view==save)
        {
           /* SharedPreferences sharedPreferences=getSharedPreferences("settings",MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putInt("radius", Integer.parseInt(radius.getText().toString()));
            editor.apply();*/
            startActivity(new Intent(SettingsActivity.this,HomeActivity.class));
        }
        if (view==soundbtn)
        {
            new LovelyChoiceDialog(SettingsActivity.this)
                    .setTopColorRes(R.color.colorPrimary)
                    .setTitle("Sound Options")
                    .setMessage("Please Choose a Sound")
                    .setIcon(R.drawable.ic_user_icon)
                    .setItems(generateString(), new LovelyChoiceDialog.OnItemSelectedListener<String>() {
                        @Override
                        public void onItemSelected(int position, String item) {
                            SharedPreferences.Editor editor;
                            editor=sharedPreferences.edit();
                            editor.putString("sound",item.toString());
                            editor.apply();
                        }
                    }).show();
        }
        if (view==radiusbtn)
        {
            final int radius=sharedPreferences.getInt("radius",500);
            new LovelyTextInputDialog(SettingsActivity.this)
                    .setTopColorRes(R.color.colorPrimary)
                    .setTitle("Radius Options")
                    .setMessage("Please Type Radius(meters)")
                    .setIcon(R.drawable.ic_user_icon)
                    .setInputType(InputType.TYPE_CLASS_NUMBER)
                    .setHint("Current Radius: " + Integer.toString(radius))
                    .setInputFilter("Please type radius", new LovelyTextInputDialog.TextFilter() {
                        @Override
                        public boolean check(String text) {
                            return text.matches("\\w+");
                        }
                    }).setConfirmButton(android.R.string.ok, new LovelyTextInputDialog.OnTextInputConfirmListener() {
                @Override
                public void onTextInputConfirmed(String text) {
                    SharedPreferences.Editor editor;
                    editor=sharedPreferences.edit();
                    editor.putInt("radius", Integer.parseInt(text.toString()));
                    editor.apply();
                }
            }).show();
        }
        if (view==reset)
        {
            SharedPreferences.Editor editor;
            editor=sharedPreferences.edit();
            editor.putInt("radius",500);
            editor.putString("sound","Default");
            editor.apply();
            startActivity(new Intent(SettingsActivity.this,HomeActivity.class));
        }
    }

    private List<String> generateString() {
        List<String> result=new ArrayList<>();
        result.add("Basic");
        result.add("Default");
        result.add("Silent");
        return result;
    }
}
