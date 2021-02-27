package com.example.news.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FabnesDAO
{
    @Query("SELECT * FROM fab")
    List<Fab> getAllNews();
    @Query("SELECT * FROM fab WHERE title=:p")
    List<Fab> getNews(String p);
    @Insert
    Void pushchoise(Fab... fab);
    @Query("DELETE FROM fab WHERE title=:p")
    void deleteAll(String p);
}
