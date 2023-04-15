package com.example.lostie.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.lostie.R;
import com.example.lostie.map.MapActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void continueMenu(View v) {
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
    }

    public void toMapActivity(View v){
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }
}