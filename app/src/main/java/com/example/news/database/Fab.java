package com.example.news.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity
public class Fab {
    @PrimaryKey(autoGenerate = true)
    public int Rid;
    @ColumnInfo(name = "title")
    public String title;
    @ColumnInfo(name = "dec")
    public String dec;
    @ColumnInfo(name = "img")
    public String img;
    @ColumnInfo(name = "link")
    public String link;
    @ColumnInfo(name = "source")
    public String source;
    @ColumnInfo(name = "date")
    public String date;
}
