package com.example.morgan.surf_spot_app.model.db;

import java.util.Date;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity(tableName = "search")
public class Search {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public double latitude;

    public double longitude;

    public int radius;

    public String type;

    @ColumnInfo(name = "use_surf_keyword")
    public Boolean useSurfKeyword;

//    @ColumnInfo(name = "search_time")
//    @TypeConverters({TimestampConverter.class})
//    public Date searchTime;
}
