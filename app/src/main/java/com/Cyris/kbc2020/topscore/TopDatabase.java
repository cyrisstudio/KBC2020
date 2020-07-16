package com.Cyris.kbc2020.topscore;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {TopEntity.class}, version = 1,exportSchema = false)
public abstract class TopDatabase extends RoomDatabase {

    private static final String DatabaseName = "TopScoreDatabase";
    private static  TopDatabase sInstance;

    public static  TopDatabase getInstance(Context context)
    {

        if(sInstance==null)
        {
            sInstance = Room.databaseBuilder(context.getApplicationContext(),TopDatabase.class,TopDatabase.DatabaseName).build();
        }
        return sInstance;
    }
    public abstract TopDao daoInterface();

}
