package com.jianyiclub.jianyi2048;

import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.jianyiclub.jianyi2048.views.MyGameView;

public class MainActivity extends AppCompatActivity {

    private MyGameView myGameView;
    private Handler handler=new Handler();
    private AdView adView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        //etSupportFragmentManager().beginTransaction().replace(R.id.container,new MainFragment()).commit();
        myGameView=(MyGameView)findViewById(R.id.myGameView);
        SharedPreferences sharedPreferences = getSharedPreferences("Preservation", MODE_PRIVATE);
        myGameView.setSharedPreferences(sharedPreferences);

        //myGameView.startGame();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                myGameView.startGame();
            }
        },500);
        //adView=(AdView)findViewById(R.id.adView);
        //AdRequest adRequest = new AdRequest.Builder().build();
        //adView.loadAd(adRequest);

    }
}
