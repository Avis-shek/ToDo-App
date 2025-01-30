package com.example.final_todo.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.final_todo.model.Register;
import com.example.final_todo.model.Todo;

import java.util.List;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertUserDetails(Register register);


    @Query("select * from user where gmail = :username and password = :password")
    LiveData<Register> validateUser(String username, String password);
}
