package com.example.morgan.surf_spot_app.model.db;

import android.content.Context;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

@Ignore
public class SearchEntityReadWriteTest {
    private SearchDao searchDao;
    private AppDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        searchDao = db.searchDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void writeUserAndReadInList() throws Exception {
        Search newSearch = new Search();
        searchDao.insertSearch(newSearch);
        List<Search> allSearches = searchDao.getAll();
        //assertThat(allSearches.get(0), equalTo(newSearch));
        Assert.assertEquals(allSearches.get(0), newSearch);
    }
}

