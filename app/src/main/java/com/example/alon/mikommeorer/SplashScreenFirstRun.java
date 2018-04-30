package com.example.alon.mikommeorer;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

/**
 * Created by alonm on 01/05/2018.
 */

public class SplashScreenFirstRun extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        SharedPreferences settings=getSharedPreferences("prefs1",0);
        boolean firstRun=settings.getBoolean("firstRun",false);
        if(firstRun==false) //if running for first time
        //Splash will load for first time
        {
            SharedPreferences.Editor editor=settings.edit();
            editor.putBoolean("firstRun",true);
            editor.commit();
            Intent i=new Intent(SplashScreenFirstRun.this,SplashScreen.class);
            startActivity(i);
            finish();
        }
        else
        {
            Intent a=new Intent(SplashScreenFirstRun.this,Login.class);
            startActivity(a);
            finish();
        }



    }
}
