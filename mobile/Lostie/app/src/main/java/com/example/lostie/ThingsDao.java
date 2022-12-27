package com.example.lostie;

import static androidx.room.OnConflictStrategy.IGNORE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ThingsDao {
    @Query("SELECT * FROM thing")
    List<Thing> getAll();

    @Query("SELECT * FROM thing WHERE uid IN (:thingIds)")
    List<Thing> loadAllByIds(int[] thingIds);

    @Query("SELECT * FROM thing WHERE uid = (:thingUid)")
    Thing findByUid(int thingUid);

    @Query("SELECT * FROM thing WHERE name LIKE :first AND " +
            "name LIKE :last LIMIT 1")
    Thing findByName(String first, String last);

    @Insert(onConflict = IGNORE)
    void insert(Thing thing);

    @Update
    void update(Thing thing);

    @Delete
    void delete(Thing thing);
}

