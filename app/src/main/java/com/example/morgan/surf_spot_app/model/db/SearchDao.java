package com.example.morgan.surf_spot_app.model.db;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface SearchDao {

    @Query("SELECT * FROM search")
    List<Search> getAll();

    @Insert
    void insertSearch(Search search);

}
