package com.example.morgan.surf_spot_app.model.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Search.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract SearchDao searchDao();

}
