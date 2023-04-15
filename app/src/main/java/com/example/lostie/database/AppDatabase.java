package com.example.lostie.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Object.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ObjectDao objectDao();
}