package com.example.lostie;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.yandex.mapkit.geometry.Point;

import java.util.Date;

@Entity
public class Thing {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "name")
    public String name;

    @TypeConverters(Converters.class)
    @ColumnInfo(name = "location")
    public Point location;

    @TypeConverters(Converters.class)
    @ColumnInfo(name = "last_time")
    public Date last_time;
}

