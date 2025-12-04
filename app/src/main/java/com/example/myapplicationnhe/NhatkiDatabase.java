package com.example.myapplicationnhe;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Nhatki.class}, version = 1, exportSchema = false)
public abstract class NhatkiDatabase extends RoomDatabase {

    private static NhatkiDatabase instance;

    public abstract NhatkiDao nhatkiDao();

    public static synchronized NhatkiDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            NhatkiDatabase.class,
                            "Nhatki_Database"
                    )
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
