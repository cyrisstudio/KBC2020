package com.Cyris.kbc2020.topscore;

import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TopDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void InsertAll(List<TopEntity> all);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void Insert(TopEntity data);

    @Query("SELECT * FROM TopEntity ORDER BY rank ASC")
    List<TopEntity> getData();


    @Query("SELECT count(*) FROM TopEntity")
    int size();



}
