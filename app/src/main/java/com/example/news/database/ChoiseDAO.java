package com.example.news.database;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ChoiseDAO
{
    @Query("SELECT * FROM choise")
    List<Choise> getChoise();
    @Insert
    Void pushchoise(Choise... choise);
    @Query("DELETE FROM choise")
    void deleteAll();
}
