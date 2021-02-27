package com.example.news.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Choise {
    @PrimaryKey(autoGenerate = true)
    public int Rid;
    @ColumnInfo(name = "key")
    public String key;
    @ColumnInfo(name = "language")
    public String language;
}
