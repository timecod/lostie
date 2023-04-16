package com.example.lostie.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ObjectDao {

    // allowing the insert of the same word multiple times by passing a
    // conflict resolution strategy
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Object object);

    @Query("DELETE FROM objects")
    void deleteAll();

    @Query("SELECT * FROM objects WHERE id=:id ")
    Object getObjectById(long id);

    @Update
    void update(Object object);

    @Delete
    void delete(Object object);

    @Query("SELECT * FROM objects")
    LiveData<List<Object>> getAll();
}
