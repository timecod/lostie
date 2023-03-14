package com.example.lostie;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import com.mapbox.maps.MapView;
import com.mapbox.maps.Style;



public class MapActivity extends AppCompatActivity {
    MapView mapView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        mapView = findViewById(R.id.mapView);
        mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS);
    }
    
}
