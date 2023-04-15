package com.example.lostie.database;


import android.content.Context;

import androidx.room.Room;

import com.example.lostie.database.AppDatabase;
import com.example.lostie.database.Object;
import com.example.lostie.database.ObjectDao;
import com.mapbox.geojson.Point;

public class DatabaseHelper {
    public AppDatabase db;
    public ObjectDao dao;


    public void init (Context context) {
        db = Room.databaseBuilder(context,
                AppDatabase.class, "objects").build();
        dao = db.objectDao();
    }

    public void update (long id, float latitude, float longitude) {
        long millis = System.currentTimeMillis();
        Object newObj = dao.getObjectById(id);
        newObj.date = millis;
        newObj.latitude = latitude;
        newObj.longitude = longitude;
        dao.update(newObj);
    }

    public void insert (long id, String name) {
        Object newObj = new Object();
        newObj.id = id;
        newObj.name = name;
        dao.insert(newObj);
    }

    public void delete (long id) {
        Object newObj = dao.getObjectById(id);
        dao.delete(newObj);
    }

    public Point getPointById (long id) {
        Object newObj = dao.getObjectById(id);
        return Point.fromLngLat(newObj.latitude, newObj.longitude);
    }

    public String getNameById (long id) {
        Object newObj = dao.getObjectById(id);
        return newObj.name;
    }



}
