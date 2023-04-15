package com.example.lostie.database;




import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "objects")
public class Object {
    @PrimaryKey
    public long id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "date")
    public long date;

    @ColumnInfo(name = "latitude")
    public float latitude;

    @ColumnInfo(name = "longitude")
    public float longitude;
}
