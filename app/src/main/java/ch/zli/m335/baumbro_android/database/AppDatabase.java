package ch.zli.m335.baumbro_android.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Tree.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TreeDao treeDao();

    private static volatile AppDatabase instance;

    public static AppDatabase getInstance(Context context){
        if(instance == null){
            synchronized (AppDatabase.class){
                if (instance == null){
                    instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "tree.db")
                            .createFromAsset("databases/db.sqlite")
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return instance;
    }




}