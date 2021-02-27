package com.example.news.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Choise.class},version = 1)

public abstract class Appdatabase extends RoomDatabase
{
    public abstract ChoiseDAO choiseDAO();
    private static Appdatabase INSTANCE;
    public static Appdatabase getDbInstance(Context context)
    {
        if(INSTANCE == null)
        {
            INSTANCE= Room.databaseBuilder(context.getApplicationContext(),Appdatabase.class,"choise")
                    .allowMainThreadQueries()
                    .build();

        }
        return INSTANCE;
    }
}
