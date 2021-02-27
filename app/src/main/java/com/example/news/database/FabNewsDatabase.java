package com.example.news.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {Fab.class},version = 1)
public abstract class FabNewsDatabase extends RoomDatabase
{
    public abstract FabnesDAO fabnesDAO();
    private static FabNewsDatabase INSTANCE;
    public static FabNewsDatabase getDbInstance(Context context)
    {
        if(INSTANCE == null)
        {
            INSTANCE= Room.databaseBuilder(context.getApplicationContext(),FabNewsDatabase.class,"fab")
                    .allowMainThreadQueries()
                    .build();

        }
        return INSTANCE;
    }
}
