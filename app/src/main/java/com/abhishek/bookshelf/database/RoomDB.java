package com.abhishek.bookshelf.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {BookEntity.class}, version = 1, exportSchema = false)
public abstract class RoomDB extends RoomDatabase {
    //Define database name
    private static final String DATABASE_NAME = "books";
    //Create database instance
    private static RoomDB database;

    public synchronized static RoomDB getInstance(Context context) {
        //Check condition
        if (database == null) {
            //When database is null
            //Initialize database
            database = Room.databaseBuilder(context.getApplicationContext(), RoomDB.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        //Return Database
        return database;
    }

    //Create DAO
    public abstract BookDao bookDao();

}
