package android.roomdatabasedemo.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.roomdatabasedemo.database.dao.DaoAccess;
import android.roomdatabasedemo.database.entity.NameEntity;



@Database(entities = {NameEntity.class}, version = 2)
public abstract class SampleDatabase extends RoomDatabase {
    public static SampleDatabase INSTANCE;
    public static String DATABASENAME = "users.sqlite";

    public abstract DaoAccess daoAccess();

    public static SampleDatabase getSampleDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room
                    .databaseBuilder(context.getApplicationContext(), SampleDatabase.class, DATABASENAME).allowMainThreadQueries().build();
        }

        // For Copy Database from Assets
       /* if (INSTANCE == null) {
            INSTANCE = RoomAsset
                    .databaseBuilder(context.getApplicationContext(), SampleDatabase.class, DATABASENAME).allowMainThreadQueries().build();
        }*/
        return INSTANCE;
    }



    public static void destroyInstance() {
        INSTANCE = null;
    }



}
