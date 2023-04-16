package com.example.lostie.database;


import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.example.lostie.database.AppDatabase;
import com.example.lostie.database.Object;
import com.example.lostie.database.ObjectDao;
import com.mapbox.geojson.Point;

import java.util.List;

public class DatabaseHelper {
    public AppDatabase db;
    public ObjectDao dao;
    private LiveData<List<Object>> all;


    DatabaseHelper (Application application) {
        db = AppDatabase.getDatabase(application);
        dao = db.objectDao();
        all = dao.getAll();
    }

    LiveData<List<Object>> getAll() {
        return all;
    }
    public void update (long id, float latitude, float longitude) {
        long millis = System.currentTimeMillis();
        Object newObj = dao.getObjectById(id);
        newObj.date = millis;
        newObj.latitude = latitude;
        newObj.longitude = longitude;
        dao.update(newObj);
    }

    public void insert (Object object) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            dao.insert(object);
        });
    }

    public void delete (Object object) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            dao.delete(object);
        });
    }

    /*public Point getPointById (long id) {
        Object newObj
        AppDatabase.databaseWriteExecutor.execute(() -> {
            dao.getObjectById(id);
        });
        return Point.fromLngLat(newObj.latitude, newObj.longitude);
    }

    public String getNameById (long id) {
        Object newObj = dao.getObjectById(id);
        return newObj.name;
    }
*/


}
