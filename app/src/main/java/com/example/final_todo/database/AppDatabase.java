package com.example.final_todo.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.final_todo.model.Category;

import com.example.final_todo.model.Register;
import com.example.final_todo.model.Todo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(exportSchema = false, version = 17, entities = {Category.class, Todo.class, Register.class})
@TypeConverters(DateConvertor.class)
public abstract class AppDatabase extends RoomDatabase {
    public static final String dbName ="TodoApp";

    public static AppDatabase sharedInstance;

    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(1);

    public static synchronized AppDatabase getInstance(Context context) {
        if(sharedInstance == null) {
            sharedInstance = Room.databaseBuilder(context, AppDatabase.class, dbName)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return sharedInstance;
    }

    public abstract CategoryDao categoryDao();

    public abstract TodoDao todoDao();
    public abstract UserDao userDao();
}
