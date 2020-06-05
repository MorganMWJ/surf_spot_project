package com.example.morgan.surf_spot_app.model.db;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Search.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    /**
     * Get instance of database using Singleton design pattern.
     * @param context
     * @return Database Instance
     */
    public static AppDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (AppDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "app_database").allowMainThreadQueries().
                            build();
                    /*
                    // Do the following when migrating to a new version of the database
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            FaaRoomDatabase.class,
                            "faa_database").addMigrations(
                            MIGRATION_1_2, MIGRATION_2_3).build();
                     */
                }
            }
        }
        return INSTANCE;
    }

//    /**
//     * Callback to populate database initially.
//     */
//    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback()(){
//        @Override
//        public void onCreate(@NonNull SupportSQLiteDatabase db){
//            super.onCreate(db);
//            new PopulateDbAsync(INSTANCE).execute();
//        }
//    }

    /**
     * Migration from DB version 1 to DB version 2.
     */
    static final Migration MIGRATION_1_2 = new Migration(1,2){
        @Override
        public void migrate(SupportSQLiteDatabase database){
            Log.d("migrate", "Doing a migratefrom version 1 to 2");
            // This is where we make relevant database datachanges,
            // or copy data from old table to a new table.
            // Deals with the migration from version 1 to version 2}};
        }
    };

    /**
     * Getter for the SearchDao.
     */
    public abstract SearchDao searchDao();


}
